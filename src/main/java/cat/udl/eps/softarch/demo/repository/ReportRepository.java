package cat.udl.eps.softarch.demo.repository;

import cat.udl.eps.softarch.demo.domain.Comment;
import cat.udl.eps.softarch.demo.domain.Event;
import cat.udl.eps.softarch.demo.domain.Report;
import cat.udl.eps.softarch.demo.domain.User;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.Optional;

@RepositoryRestResource
public interface ReportRepository extends CrudRepository<Report, Long>, PagingAndSortingRepository<Report, Long> {

    String ADMIN_CLAUSE = "?#{hasRole('ADMIN')} = true";

    @Override
    @Query("SELECT r FROM Report r WHERE " + ADMIN_CLAUSE)
    Page<Report> findAll(Pageable pageable);

    @Override
    @Query("SELECT r FROM Report r WHERE r.id = :id AND " + ADMIN_CLAUSE)
    Optional<Report> findById(@Param("id") Long id);

    @RestResource(exported = false)
    boolean existsByReporterAndEvent(User reporter, Event event);

    @RestResource(exported = false)
    boolean existsByReporterAndComment(User reporter, Comment comment);

    @RestResource(exported = false)
    long countByReporter_IdAndEvent_Id(String reporterId, Long eventId);

    @RestResource(exported = false)
    long countByReporter_IdAndComment_Id(String reporterId, Long commentId);
}
