package cat.udl.eps.softarch.demo.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

// Order -99 places this filter after the Spring Security chain (-100), so anonymous requests still get 401
@Component
@Order(-99)
public class UsernameImmutabilityFilter extends OncePerRequestFilter {
    private static final String USERS_PREFIX = "/users/";

    private final ObjectMapper objectMapper;

    public UsernameImmutabilityFilter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        String targetUsername = targetUsername(request);
        if (targetUsername == null || !isUpdate(request)) {
            chain.doFilter(request, response);
            return;
        }

        // The body is read here, so it is cached for the handlers that run after this filter
        byte[] body = request.getInputStream().readAllBytes();
        JsonNode requested = requestedUsername(body);
        if (requested != null && requested.isString() && !requested.stringValue().equals(targetUsername)) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.getWriter().write("{\"message\":\"Username cannot be changed\"}");
            return;
        }

        chain.doFilter(new CachedBodyRequest(request, body), response);
    }

    private static String targetUsername(HttpServletRequest request) {
        String path = request.getRequestURI().substring(request.getContextPath().length());
        if (!path.startsWith(USERS_PREFIX)) {
            return null;
        }
        String username = path.substring(USERS_PREFIX.length());
        return username.isEmpty() || username.contains("/") ? null : username;
    }

    private static boolean isUpdate(HttpServletRequest request) {
        return HttpMethod.PUT.matches(request.getMethod()) || HttpMethod.PATCH.matches(request.getMethod());
    }

    private JsonNode requestedUsername(byte[] body) {
        try {
            return objectMapper.readTree(body).get("username");
        } catch (JacksonException e) {
            // Malformed bodies are left for Spring Data REST to reject
            return null;
        }
    }

    private static final class CachedBodyRequest extends HttpServletRequestWrapper {
        private final byte[] body;

        CachedBodyRequest(HttpServletRequest request, byte[] body) {
            super(request);
            this.body = body;
        }

        @Override
        public ServletInputStream getInputStream() {
            ByteArrayInputStream stream = new ByteArrayInputStream(body);
            return new ServletInputStream() {
                @Override
                public boolean isFinished() {
                    return stream.available() == 0;
                }

                @Override
                public boolean isReady() {
                    return true;
                }

                @Override
                public void setReadListener(ReadListener listener) {
                    throw new UnsupportedOperationException("Asynchronous reads are not supported");
                }

                @Override
                public int read() {
                    return stream.read();
                }
            };
        }

        @Override
        public BufferedReader getReader() {
            return new BufferedReader(new InputStreamReader(getInputStream(), StandardCharsets.UTF_8));
        }
    }
}
