package cat.udl.eps.softarch.demo.repository;

import cat.udl.eps.softarch.demo.domain.Event;
import cat.udl.eps.softarch.demo.domain.User;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

@RepositoryRestResource
public interface EventRepository extends CrudRepository<Event, Long>, PagingAndSortingRepository<Event, Long> {

    String OWNERSHIP_CLAUSE =
        "e.ownedBy.id = ?#{authentication.name} " +
        "OR ?#{hasRole('ADMIN')} = true";

    @Override
    @Query("SELECT e FROM Event e WHERE " + OWNERSHIP_CLAUSE)
    Page<Event> findAll(Pageable pageable);

    @Override
    @Query("SELECT e FROM Event e WHERE e.id = :id AND (" + OWNERSHIP_CLAUSE + ")")
    Optional<Event> findById(@Param("id") Long id);

    @Query("SELECT e FROM Event e WHERE e.ownedBy = :user AND (" + OWNERSHIP_CLAUSE + ")")
    Page<Event> findByOwnedBy(@Param("user") User owner, Pageable pageable);
}
