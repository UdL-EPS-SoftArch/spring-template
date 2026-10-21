package cat.udl.eps.softarch.demo.handler;

import cat.udl.eps.softarch.demo.domain.User;
import cat.udl.eps.softarch.demo.repository.UserRepository;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.rest.core.RepositoryConstraintViolationException;
import org.springframework.data.rest.core.annotation.HandleAfterCreate;
import org.springframework.data.rest.core.annotation.HandleAfterDelete;
import org.springframework.data.rest.core.annotation.HandleAfterLinkSave;
import org.springframework.data.rest.core.annotation.HandleAfterSave;
import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.HandleBeforeDelete;
import org.springframework.data.rest.core.annotation.HandleBeforeLinkSave;
import org.springframework.data.rest.core.annotation.HandleBeforeSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
<<<<<<< Updated upstream
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

import java.util.Set;
=======
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.util.UriUtils;

import java.nio.charset.StandardCharsets;
>>>>>>> Stashed changes

@Component
@RepositoryEventHandler
public class UserEventHandler {

    final Logger logger = LoggerFactory.getLogger(User.class);

    final UserRepository userRepository;
    final Validator validator;

    public UserEventHandler(UserRepository userRepository, Validator validator) {
        this.userRepository = userRepository;
        this.validator = validator;
    }

    @HandleBeforeCreate
    public void handleUserPreCreate(User user) {
        user.setAdmin(false);
        logger.info("Before creating: {}", user.toString());
    }

    @HandleBeforeSave
    public void handleUserPreSave(User user) {
        ensureCurrentUserOrAdmin();
        logger.info("Before updating: {}", user.toString());
        ensureSelfOrAdmin(user);
        encodeNewPassword(user);
    }

    // A PATCH without a password keeps the stored BCrypt hash, so only a plain-text value is validated and encoded.
    // Validation runs here because the field constraints would otherwise check the hash instead of the password
    private void encodeNewPassword(User user) {
        String password = user.getPassword();
        if (password == null || password.matches("\\$2[aby]\\$\\d{2}\\$.{53}")) {
            return;
        }
        Set<ConstraintViolation<User>> violations = validator.validateValue(User.class, "password", password);
        if (!violations.isEmpty()) {
            Errors errors = new BeanPropertyBindingResult(user, "user");
            violations.forEach(violation -> errors.rejectValue("password", "invalid", violation.getMessage()));
            throw new RepositoryConstraintViolationException(errors);
        }
        user.encodePassword();
    }

    @HandleBeforeDelete
    public void handleUserPreDelete(User user) {
        ensureCurrentUserOrAdmin();
        logger.info("Before deleting: {}", user.toString());
        ensureSelfOrAdmin(user);
    }

    private void ensureSelfOrAdmin(User user) {
        User caller = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!caller.isAdmin() && !caller.getUsername().equals(user.getUsername())) {
            throw new ResourceNotFoundException("User not found");
        }
    }

    @HandleBeforeLinkSave
    public void handleUserPreLinkSave(User user, Object o) {
        logger.info("Before linking: {} to {}", user.toString(), o.toString());
    }

    @HandleAfterCreate
    public void handleUserPostCreate(User user) {
        logger.info("After creating: {}", user.toString());
        user.encodePassword();
        userRepository.save(user);
    }

    @HandleAfterSave
    public void handleUserPostSave(User user) {
        logger.info("After updating: {}", user.toString());
        userRepository.save(user);
    }

    @HandleAfterDelete
    public void handleUserPostDelete(User user) {
        logger.info("After deleting: {}", user.toString());
    }

    @HandleAfterLinkSave
    public void handleUserPostLinkSave(User user, Object o) {
        logger.info("After linking: {} to {}", user.toString(), o.toString());
    }

    private void ensureCurrentUserOrAdmin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getAuthorities().stream()
                .anyMatch(authority -> "ROLE_ADMIN".equals(authority.getAuthority()))) {
            return;
        }

        if (!isRequestForAuthenticatedUser(authentication)) {
            throw new AccessDeniedException("Users can only manage their own profile");
        }
    }

    private boolean isRequestForAuthenticatedUser(Authentication authentication) {
        if (!(RequestContextHolder.getRequestAttributes() instanceof ServletRequestAttributes requestAttributes)) {
            return false;
        }

        String requestPath = requestAttributes.getRequest().getRequestURI()
                .substring(requestAttributes.getRequest().getContextPath().length());
        int usersPathIndex = requestPath.indexOf("/users/");
        if (usersPathIndex < 0) {
            return false;
        }

        String requestedUserId = requestPath.substring(usersPathIndex + "/users/".length()).split("/", 2)[0];
        return authentication.getName().equals(UriUtils.decode(requestedUserId, StandardCharsets.UTF_8));
    }
}
