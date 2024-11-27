package toy.hospital.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Patient {

    @Id @GeneratedValue
    @Column(name = "patient_id")
    private Long id;

    private Integer age;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private String name;

    @OneToMany(mappedBy = "patient")
    public List<Reserve> reserves = new ArrayList<>();
}
