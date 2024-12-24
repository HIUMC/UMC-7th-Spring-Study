package toyproject.hongikhospital.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

@Entity
@Getter @Setter
public class Hospital {
    @Id
    @GeneratedValue
    @Column(name="hospital_id")
    private Long id;

    private String Name;

    private String Address;

}
