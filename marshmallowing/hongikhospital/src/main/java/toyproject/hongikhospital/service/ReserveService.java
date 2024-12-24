package toyproject.hongikhospital.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import toyproject.hongikhospital.controller.ReservationForm;
import toyproject.hongikhospital.domain.Doctor;
import toyproject.hongikhospital.domain.Patient;
import toyproject.hongikhospital.domain.Reservation;
import toyproject.hongikhospital.repository.DoctorRepository;
import toyproject.hongikhospital.repository.PatientRepository;
import toyproject.hongikhospital.repository.ReserveRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReserveService {
    private final ReserveRepository reserveRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;

    @Transactional
    public Long createReservation(Long patientId, Long doctorId, ReservationForm reservationForm) {

        //엔티티 조회
        Patient patient = patientRepository.findById(patientId);
        Doctor doctor = doctorRepository.findById(doctorId);
        //ReservationForm reservation=reservationForm.

        reserveRepository.save(reservation);

        return reservation.getId();
    }
}
