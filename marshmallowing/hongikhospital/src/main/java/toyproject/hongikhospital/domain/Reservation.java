package toyproject.hongikhospital.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Getter @Setter
public class Reservation {

    @Id
    @GeneratedValue
    @Column(name="reserve_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="doctor_id")
    private Doctor doctor;

    @OneToOne(cascade = CascadeType.ALL
            , fetch = FetchType.LAZY)
    @JoinColumn(name="patient_id")
    private Patient patient;

    private LocalDateTime Time;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(nullable = true)
    private Integer Price;

}
