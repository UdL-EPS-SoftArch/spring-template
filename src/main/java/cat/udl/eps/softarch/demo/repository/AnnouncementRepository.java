package cat.udl.eps.softarch.demo.repository;

import cat.udl.eps.softarch.demo.domain.Announcement;
import cat.udl.eps.softarch.demo.domain.Request;
import cat.udl.eps.softarch.demo.domain.User;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

public interface AnnouncementRepository extends PagingAndSortingRepository<Announcement, Long> {

    List<Announcement> findById(@Param("id") long id);
}
