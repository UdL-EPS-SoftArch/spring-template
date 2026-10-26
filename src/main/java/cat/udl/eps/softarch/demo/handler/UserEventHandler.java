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
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

import java.util.Set;

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
        ensureSelfOrAdmin(user);
        logger.info("Before updating: {}", user.toString());
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
        ensureSelfOrAdmin(user);
        logger.info("Before deleting: {}", user.toString());
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

}
