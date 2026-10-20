package cat.udl.eps.softarch.demo.controller;

import cat.udl.eps.softarch.demo.domain.Event;
import cat.udl.eps.softarch.demo.domain.User;
import cat.udl.eps.softarch.demo.repository.EventRepository;
import org.springframework.data.rest.webmvc.BasePathAwareController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@BasePathAwareController
public class EventParticipantsController {

    private final EventRepository eventRepository;

    public EventParticipantsController(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @GetMapping("/events/{id}/participants")
    public @ResponseBody List<String> listParticipants(@PathVariable Long id) {
        return findEvent(id).getParticipants().stream().map(User::getUri).toList();
    }

    @PostMapping("/events/{id}/participants")
    public ResponseEntity<Void> join(@PathVariable Long id) {
        Event event = findEvent(id);
        String username = currentUsername();
        if (isRegistered(event, username)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Already registered for this event");
        }
        if (event.getParticipants().size() >= event.getMaxParticipants()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Event is full");
        }
        event.getParticipants().add((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        eventRepository.save(event);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/events/{id}/participants")
    public ResponseEntity<Void> leave(@PathVariable Long id) {
        Event event = findEvent(id);
        String username = currentUsername();
        if (!isRegistered(event, username)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not registered for this event");
        }
        event.getParticipants().removeIf(participant -> participant.getUsername().equals(username));
        eventRepository.save(event);
        return ResponseEntity.noContent().build();
    }

    private Event findEvent(Long id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found"));
    }

    private String currentUsername() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    private boolean isRegistered(Event event, String username) {
        return event.getParticipants().stream().anyMatch(participant -> participant.getUsername().equals(username));
    }
}
