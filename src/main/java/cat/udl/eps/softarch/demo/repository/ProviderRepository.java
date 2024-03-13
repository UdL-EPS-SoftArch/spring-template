package cat.udl.eps.softarch.demo.repository;

import cat.udl.eps.softarch.demo.domain.Provider;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
public interface ProviderRepository extends PagingAndSortingRepository<Provider, String> {

    List<Provider> findByUsernameContaining(@Param("text") String text);
    Provider findByUsernameAndPassword(@Param("username") String username,@Param("password") String password);

//    void save(Provider provider);
}