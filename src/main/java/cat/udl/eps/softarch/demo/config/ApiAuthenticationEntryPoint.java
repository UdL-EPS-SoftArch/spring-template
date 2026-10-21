package cat.udl.eps.softarch.demo.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class ApiAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private static final String BASIC_PREFIX = "Basic ";

    private final BasicAuthenticationEntryPoint basicEntryPoint = new BasicAuthenticationEntryPoint();

    public ApiAuthenticationEntryPoint(String realmName) {
        basicEntryPoint.setRealmName(realmName);
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException exception) throws IOException {
        if (exception instanceof LockedException || exception instanceof DisabledException) {
            response.sendError(HttpStatus.FORBIDDEN.value(), "Account suspended");
        } else if (hasOneBlankCredential(request)) {
            response.sendError(HttpStatus.BAD_REQUEST.value(), "Username and password are required");
        } else {
            basicEntryPoint.commence(request, response, exception);
        }
    }

    // Both credentials blank is the unauthenticated state of a client, so only a half-filled login is a bad request
    private boolean hasOneBlankCredential(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith(BASIC_PREFIX)) {
            return false;
        }
        try {
            String decoded = new String(Base64.getDecoder().decode(header.substring(BASIC_PREFIX.length()).trim()),
                    StandardCharsets.UTF_8);
            int separator = decoded.indexOf(':');
            return separator >= 0 && (separator == 0) != (separator == decoded.length() - 1);
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
