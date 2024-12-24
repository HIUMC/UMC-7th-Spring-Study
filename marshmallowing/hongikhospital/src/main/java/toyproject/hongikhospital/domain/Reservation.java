package toyproject.hongikhospital.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import toyproject.hongikhospital.domain.enums.Status;

import java.time.LocalDateTime;

@Entity
@Getter @Setter
public class Reservation {
//왜 일대다에서 다가 외래키를 가지지
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

    private Integer Price = 0;
    //@(Column 어쩌구) 옵션 넣어도됨
    //default 값 사용해서 nullable 처리같이
}