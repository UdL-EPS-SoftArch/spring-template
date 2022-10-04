package cat.udl.eps.softarch.demo.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.DecimalMin;

import javax.validation.constraints.NotEmpty;
import java.math.BigDecimal;

@Entity
@Data
@EqualsAndHashCode(callSuper = false)
public class Announcement {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NotEmpty
    @Length(min = 2, max = 24)
    private String name;

    @NotEmpty
    @DecimalMin(value = "0")
    private BigDecimal price;

    @NotEmpty
    @Length(min = 10, max = 200)
    private String description;
}
