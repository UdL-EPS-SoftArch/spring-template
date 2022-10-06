package cat.udl.eps.softarch.demo.domain;

import com.fasterxml.jackson.annotation.JsonIdentityReference;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class Offer extends Announcement {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private ZonedDateTime dateTime;

    @ManyToOne
    @JsonIdentityReference(alwaysAsId = true)
    private User offerer;

}
