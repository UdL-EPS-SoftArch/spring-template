package cat.udl.eps.softarch.demo.domain;

<<<<<<< HEAD
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import java.time.ZonedDateTime;
import java.util.Date;
=======
import java.util.Date;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import lombok.Data;

import org.hibernate.validator.constraints.Length;
>>>>>>> reviews/scenarios

@Entity
@Data
@EqualsAndHashCode(callSuper = false)
public class Review {

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private int id;

<<<<<<< HEAD
    private ZonedDateTime when = ZonedDateTime.now();

    @NotNull(message = "Must provide a valid number of stars in your review")
    @Min(1)
=======
    private Date when = new Date();

    @NotNull(message = "You must to provide a number of stars in your review")
    @Min(0)
>>>>>>> reviews/scenarios
    @Max(5)
    private Integer stars;

    @Length(max = 256)
    private String message;

    @ManyToOne
    @NotNull
    @JsonIdentityReference(alwaysAsId = true)
    private User author;

    @ManyToOne
    @NotNull
    @JsonIdentityReference(alwaysAsId = true)
    private User about;
}
