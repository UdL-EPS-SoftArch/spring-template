package cat.udl.eps.softarch.demo.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

// Spring Data REST does not open a transaction around association updates. Running the whole request in one keeps
// the pessimistic lock taken by EventEventHandler until commit, so concurrent joins cannot exceed maxParticipants.
// Order -99 places this filter after the Spring Security chain (-100).
@Component
@Order(-99)
public class ParticipationTransactionFilter extends OncePerRequestFilter {
    private final TransactionTemplate transactionTemplate;

    public ParticipationTransactionFilter(TransactionTemplate transactionTemplate) {
        this.transactionTemplate = transactionTemplate;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return !request.getRequestURI().matches("/events/[^/]+/participants(/.*)?");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        try {
            transactionTemplate.executeWithoutResult(status -> {
                try {
                    chain.doFilter(request, response);
                    if (response.getStatus() >= 400) {
                        status.setRollbackOnly();
                    }
                } catch (ServletException | IOException e) {
                    throw new IllegalStateException(e);
                }
            });
        } catch (IllegalStateException e) {
            if (e.getCause() instanceof ServletException servletException) {
                throw servletException;
            }
            if (e.getCause() instanceof IOException ioException) {
                throw ioException;
            }
            throw e;
        }
    }
}
