package cat.udl.eps.softarch.demo.repository;

import cat.udl.eps.softarch.demo.domain.Subcategory;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface SubcategoryRepository extends CrudRepository<Subcategory, Long>,
        PagingAndSortingRepository<Subcategory, Long> {

    Optional<Subcategory> findByNameAndCategory_Name(String name, String categoryName);

    boolean existsByNameAndCategory_Name(String name, String categoryName);
}
