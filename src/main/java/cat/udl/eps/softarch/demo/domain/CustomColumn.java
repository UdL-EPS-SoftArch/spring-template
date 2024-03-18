package cat.udl.eps.softarch.demo.domain;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.config.Projection;

@Projection(
    name = "customColumn",
    types = { Column.class }
)

public interface CustomColumn {
    Long getId();
    String getTitle();
    @Value("#{target.columnBelongsTo.title}")
    String getProvideBy();
}
