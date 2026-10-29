package cat.udl.eps.softarch.demo.handler;

import cat.udl.eps.softarch.demo.domain.Event;
import cat.udl.eps.softarch.demo.domain.User;
import cat.udl.eps.softarch.demo.repository.EventRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.data.rest.core.annotation.*;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashSet;
import java.util.Set;

@Component
@RepositoryEventHandler
public class EventEventHandler {

    private final EntityManagerFactory entityManagerFactory;

    private final EventRepository eventRepository;

    public EventEventHandler(EntityManagerFactory entityManagerFactory, EventRepository eventRepository) {
        this.entityManagerFactory = entityManagerFactory;
        this.eventRepository = eventRepository;
    }

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

    @HandleBeforeLinkSave
    public void handleEventPreLinkSave(Event event, Object linked) {
        if (!(linked instanceof Set<?>)) {
            return;
        }
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        eventRepository.findByIdForUpdate(event.getId());
        Set<String> before = persistedParticipants(event.getId());
        Set<String> after = usernames(event.getParticipants());

        Set<String> changed = new HashSet<>(after);
        changed.addAll(before);
        changed.removeIf(name -> before.contains(name) == after.contains(name));
        if (!changed.stream().allMatch(username::equals)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You can only join or leave an event yourself");
        }
        Set<String> joined = new HashSet<>(after);
        joined.removeAll(before);
        if (!joined.isEmpty() && before.size() + joined.size() > event.getMaxParticipants()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Event is full");
        }
    }

    private Set<String> persistedParticipants(Long eventId) {
        // A fresh persistence context: the request-scoped one already holds the modified collection
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            return new HashSet<>(entityManager.createQuery(
                    "SELECT p.id FROM Event e JOIN e.participants p WHERE e.id = :id", String.class)
                    .setParameter("id", eventId)
                    .getResultList());
        }
    }

    private Set<String> usernames(Set<User> users) {
        Set<String> names = new HashSet<>();
        users.forEach(user -> names.add(user.getUsername()));
        return names;
    }

    private void ensureOwner(Event event) {
        User owner = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!owner.isAdmin() && !owner.getUsername().equals(event.getCreator().getUsername())) {
            throw new ResourceNotFoundException("Event not found");
        }
    }
}
