package cat.udl.eps.softarch.demo.repository;

import cat.udl.eps.softarch.demo.domain.Event;
import cat.udl.eps.softarch.demo.domain.User;

import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;
import java.util.Optional;

@RepositoryRestResource
public interface EventRepository extends CrudRepository<Event, Long>, PagingAndSortingRepository<Event, Long> {

    String OWNERSHIP_CLAUSE =
        "e.creator.id = ?#{authentication.name} " +
        "OR ?#{hasRole('ADMIN')} = true";

    @Query("SELECT e FROM Event e WHERE e.creator = :user AND (" + OWNERSHIP_CLAUSE + ")")
    Page<Event> findByCreator(@Param("user") User creator, Pageable pageable);

    List<Event> findBySubcategory_Category_NameIgnoreCase(@Param("name") String name);

    List<Event> findByTitleContainingIgnoreCase(@Param("keyword") String keyword);

    List<Event> findByTitleContainingIgnoreCaseAndSubcategory_Category_NameIgnoreCase(
        @Param("keyword") String keyword, @Param("category") String category);

    @RestResource(exported = false)
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT e FROM Event e WHERE e.id = :id")
    Optional<Event> findByIdForUpdate(@Param("id") Long id);
}
