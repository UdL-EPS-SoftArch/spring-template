package cat.udl.eps.softarch.demo.handler;

import cat.udl.eps.softarch.demo.domain.Event;
import cat.udl.eps.softarch.demo.domain.User;
import cat.udl.eps.softarch.demo.repository.EventRepository;
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
public class EventEventHandler {

    private final EventRepository eventRepository;

    public EventEventHandler(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @HandleBeforeCreate
    public void handleBeforeCreate(Event event) {
        User creator = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        event.setCreator(creator);
    }

    @HandleBeforeSave
    public void handleBeforeSave(Event event) {
        ensureCreator(event, false);
    }

    @HandleBeforeDelete
    public void handleBeforeDelete(Event event) {
        ensureCreator(event, true);
    }

    private void ensureCreator(Event event, boolean allowAdmin) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (allowAdmin && authentication.getAuthorities().stream()
                .anyMatch(authority -> "ROLE_ADMIN".equals(authority.getAuthority()))) {
            return;
        }

        Event existingEvent = eventRepository.findById(event.getId())
                .orElseThrow(() -> new IllegalArgumentException("Event does not exist"));

        if (!existingEvent.getCreator().getId().equals(authentication.getName())) {
            throw new AccessDeniedException("Only the event creator can modify or delete this event");
        }
    }
}
