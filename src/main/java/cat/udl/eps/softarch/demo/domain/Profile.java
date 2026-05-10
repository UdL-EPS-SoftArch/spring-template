package cat.udl.eps.softarch.demo.domain;

import jakarta.persistence.*;
import lombok.Data;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
@Data
@EqualsAndHashCode(exclude = "creator")
@ToString(exclude = "creator")
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public enum Visibility {
        PUBLIC,
        PRIVATE
    }

    private String description;

    @Enumerated(EnumType.STRING)
    private Visibility visibility;

    @OneToOne(mappedBy = "profile")
    private Creator creator;

    public Profile() {}
}