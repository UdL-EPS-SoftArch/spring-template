package cat.udl.eps.softarch.demo.domain;
import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.ZonedDateTime;
@Entity
@Data
@EqualsAndHashCode

public class Message {

    @Id
    private String id;

    private ZonedDateTime when;

    private String text;

}
