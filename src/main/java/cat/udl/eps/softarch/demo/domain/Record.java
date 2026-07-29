package cat.udl.eps.softarch.demo.domain;

import com.fasterxml.jackson.annotation.JsonIdentityReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.ZonedDateTime;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class Record extends UriEntity<Long> {

    public enum Status {
        PUBLIC, PRIVATE
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;

    @Length(max = 500)
    private String description;

    @Enumerated(EnumType.STRING)
    private Status status = Status.PRIVATE;

    @DateTimeFormat
    private ZonedDateTime created;

    @DateTimeFormat
    private ZonedDateTime modified;

    @ManyToOne
    @JsonIdentityReference(alwaysAsId = true)
    private User ownedBy;


}
