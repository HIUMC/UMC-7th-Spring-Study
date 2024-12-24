package toyproject.hongikhospital.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

@Entity
@Getter @Setter
public class Department {

    @Id
    @GeneratedValue
    @Column(name="department_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="hospital_id")
    private Hospital hospital;

    private String Name;

    private String Number;

}
