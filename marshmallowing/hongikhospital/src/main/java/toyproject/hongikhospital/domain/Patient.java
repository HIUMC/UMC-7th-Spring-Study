package toyproject.hongikhospital.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

@Entity
@Getter @Setter
public class Patient {

    @Id
    @GeneratedValue
    @Column(name="patient_id")
    private Long id;

    private Integer age;

    @Enumerated(EnumType.STRING)
    private Gender gender;

}
