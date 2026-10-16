package cat.udl.eps.softarch.demo.repository;

import cat.udl.eps.softarch.demo.domain.Subcategory;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
public interface SubcategoryRepository extends CrudRepository<Subcategory, Long>, PagingAndSortingRepository<Subcategory, Long> {

    List<Subcategory> findByName(String name);
}
