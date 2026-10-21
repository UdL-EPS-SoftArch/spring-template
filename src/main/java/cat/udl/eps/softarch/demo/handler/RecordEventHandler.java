package cat.udl.eps.softarch.demo.handler;

import cat.udl.eps.softarch.demo.domain.Record;
import cat.udl.eps.softarch.demo.domain.User;
import cat.udl.eps.softarch.demo.repository.RecordRepository;
import org.springframework.data.rest.core.annotation.*;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RepositoryEventHandler
public class RecordEventHandler {
    final RecordRepository recordRepository;

    public RecordEventHandler(RecordRepository recordRepository) {
        this.recordRepository = recordRepository;
    }

    @HandleBeforeCreate
    public void handleRecordPreCreate(Record record) {
        User owner = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        record.setOwnedBy(owner);
        if (record.getStatus() == null) {
            record.setStatus(Record.Status.PRIVATE);
        }
    }

    @HandleBeforeSave
    public void handleRecordPreSave(Record record) {
        ensureOwnerOrAdmin(record, "modify");
    }

    @HandleBeforeDelete
    public void handleRecordPreDelete(Record record) {
        ensureOwnerOrAdmin(record, "delete");
    }

    @HandleBeforeLinkSave
    public void handleRecordPreLinkSave(Record record, Object linkedEntity) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!isAdmin(authentication)) {
            throw new AccessDeniedException("Only administrators can change record relationships");
        }
    }

    private void ensureOwnerOrAdmin(Record record, String action) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!isAdmin(authentication)
                && (record.getOwnedBy() == null
                    || !authentication.getName().equals(record.getOwnedBy().getUsername()))) {
            throw new AccessDeniedException("Only the owner or an administrator can " + action + " the record");
        }
    }

    private boolean isAdmin(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .anyMatch(authority -> "ROLE_ADMIN".equals(authority.getAuthority()));
    }
}
