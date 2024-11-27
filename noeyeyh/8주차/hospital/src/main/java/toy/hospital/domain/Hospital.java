package toy.hospital.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class Hospital {

    @Id @GeneratedValue
    @Column(name = "hospital_id")
    private long id;

    private String name;

    private String address;
}
