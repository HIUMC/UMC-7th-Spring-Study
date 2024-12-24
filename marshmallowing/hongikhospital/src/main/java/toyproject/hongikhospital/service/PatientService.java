package toyproject.hongikhospital.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import toyproject.hongikhospital.domain.Doctor;
import toyproject.hongikhospital.domain.Patient;
import toyproject.hongikhospital.repository.PatientRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PatientService {
    private final PatientRepository patientRepository;

    //의사 등록
    @Transactional
    public Long join(Patient patient) {
        patientRepository.save(patient);
        return patient.getId();
    }
}
