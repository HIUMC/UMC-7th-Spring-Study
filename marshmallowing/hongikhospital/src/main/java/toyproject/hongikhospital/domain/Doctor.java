package toyproject.hongikhospital.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

@Entity
@Getter @Setter
public class Doctor {

    @Id
    @GeneratedValue
    @Column(name="doctor_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="department_id")
    private Department department;

    private String Name;

    private Integer Experience;
}
