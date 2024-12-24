package toyproject.hongikhospital.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import toyproject.hongikhospital.domain.Doctor;
import toyproject.hongikhospital.repository.DoctorRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DoctorService {

    public final DoctorRepository doctorRepository;

    //의사 등록
    @Transactional
    public Long join(Doctor doctor) {
        doctorRepository.save(doctor);
        return doctor.getId();
    }

    //의사 조회
    public Doctor find(Long doctorId) {
        return doctorRepository.findById(doctorId);
    }

}
