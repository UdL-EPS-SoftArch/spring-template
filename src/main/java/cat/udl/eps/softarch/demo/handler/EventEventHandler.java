package cat.udl.eps.softarch.demo.handler;

import cat.udl.eps.softarch.demo.domain.Event;
import cat.udl.eps.softarch.demo.domain.User;
import org.springframework.data.rest.core.annotation.*;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RepositoryEventHandler
public class EventEventHandler {

    @HandleBeforeCreate
    public void handleEventPreCreate(Event event) {
        User owner = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        event.setCreator(owner);
    }

    @HandleBeforeSave
    public void handleEventPreSave(Event event) {
        ensureOwner(event);
    }

    @HandleBeforeDelete
    public void handleEventPreDelete(Event event) {
        ensureOwner(event);
    }

    private void ensureOwner(Event event) {
        User owner = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!owner.isAdmin() && !owner.getUsername().equals(event.getCreator().getUsername())) {
            throw new ResourceNotFoundException("Event not found");
        }
    }
}
