package cat.udl.eps.softarch.demo.repository;

import cat.udl.eps.softarch.demo.domain.Event;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
public interface EventRepository extends CrudRepository<Event, Long>,
        PagingAndSortingRepository<Event, Long> {

    List<Event> findByTitleContainingIgnoreCase(String title);

    List<Event> findBySubcategory_Id(Long subcategoryId);

    List<Event> findByCreator_Id(String creatorId);
}
