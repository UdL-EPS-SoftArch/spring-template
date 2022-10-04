package cat.udl.eps.softarch.demo.domain;


import javax.persistence.*;

import javax.validation.constraints.NotEmpty;

import lombok.Data;
import lombok.EqualsAndHashCode;


@Entity
@Data
@EqualsAndHashCode(callSuper = true)
//@Table(name = "Request")

public class Request extends Announcement{

    @NotEmpty
   // @NotBlank
    // @ForeignKey
    private String requester;
}
