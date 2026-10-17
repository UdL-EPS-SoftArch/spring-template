package cat.udl.eps.softarch.demo.repository;

import cat.udl.eps.softarch.demo.domain.EventParticipant;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

@RepositoryRestResource(exported = false)
public interface EventParticipantRepository extends CrudRepository<EventParticipant, Long>,
        PagingAndSortingRepository<EventParticipant, Long> {

    Optional<EventParticipant> findByEvent_IdAndUser_Id(Long eventId, String userId);

    boolean existsByEvent_IdAndUser_Id(Long eventId, String userId);

    long countByEvent_Id(Long eventId);

    void deleteByEvent_IdAndUser_Id(Long eventId, String userId);
}
