package toyproject.hongikhospital.controller;

import lombok.Getter;
import lombok.Setter;
import toyproject.hongikhospital.domain.enums.Status;

import java.time.LocalDateTime;

@Getter
@Setter
public class ReservationForm {
    private Long patientId;  // 환자 ID
    private Long doctorId;   // 의사 ID
    private LocalDateTime time;  // 예약 시간
    private Status status;  // 예약 상태
    private Integer price;  // 예약 가격
}
