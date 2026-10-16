package cat.udl.eps.softarch.demo.repository;

import cat.udl.eps.softarch.demo.domain.Comment;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
public interface CommentRepository extends CrudRepository<Comment, Long>,
        PagingAndSortingRepository<Comment, Long> {

    List<Comment> findByEvent_Id(Long eventId);
}
