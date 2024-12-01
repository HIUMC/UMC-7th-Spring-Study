package toy.hospital.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter @Setter
public class Department {

    @Id @GeneratedValue
    @Column(name = "department_id")
    private long id;

    @OneToMany(mappedBy = "department")
    private List<Doctor> doctors;

    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hospital_id")
    private Hospital hospital;
}
