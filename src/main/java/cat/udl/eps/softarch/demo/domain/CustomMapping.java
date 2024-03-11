package cat.udl.eps.softarch.demo.domain;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.config.Projection;

@Projection(
    name = "customMapping",
    types = { Mapping.class }
)

public interface CustomMapping {
    Long getId();
    String getTitle();
    @Value("#{target.providedBy.id}")
    String getProvideBy();
}
