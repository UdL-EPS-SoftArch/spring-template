package cat.udl.eps.softarch.demo.repository;

import cat.udl.eps.softarch.demo.domain.CustomMapping;
import cat.udl.eps.softarch.demo.domain.Mapping;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.Optional;

@RepositoryRestResource(excerptProjection = CustomMapping.class)
public interface MappingRepository extends PagingAndSortingRepository<Mapping, Long>, CrudRepository<Mapping, Long>{
    List<Mapping> findByTitle(@Param("title") String title);
    @Override
    public Iterable<Mapping> findAll(Sort sort);

    @Override
    public Page<Mapping> findAll(Pageable pageable);

    public Optional<Mapping> findById(Long id);
}
