package cat.udl.eps.softarch.demo.handler;

import cat.udl.eps.softarch.demo.domain.Comment;
import cat.udl.eps.softarch.demo.domain.User;
import cat.udl.eps.softarch.demo.repository.CommentRepository;
import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.HandleBeforeDelete;
import org.springframework.data.rest.core.annotation.HandleBeforeSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RepositoryEventHandler
public class CommentEventHandler {

    private final CommentRepository commentRepository;

    public CommentEventHandler(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @HandleBeforeCreate
    public void handleBeforeCreate(Comment comment) {
        User author = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        comment.setAuthor(author);
    }

    @HandleBeforeSave
    public void handleBeforeSave(Comment comment) {
        ensureAuthor(comment, false);
    }

    @HandleBeforeDelete
    public void handleBeforeDelete(Comment comment) {
        ensureAuthor(comment, true);
    }

    private void ensureAuthor(Comment comment, boolean allowAdmin) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (allowAdmin && authentication.getAuthorities().stream()
                .anyMatch(authority -> "ROLE_ADMIN".equals(authority.getAuthority()))) {
            return;
        }

        Comment existingComment = commentRepository.findById(comment.getId())
                .orElseThrow(() -> new IllegalArgumentException("Comment does not exist"));

        if (!existingComment.getAuthor().getId().equals(authentication.getName())) {
            throw new AccessDeniedException("Only the comment author can modify or delete this comment");
        }
    }
}
