package toyproject.hongikhospital.repository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import toyproject.hongikhospital.domain.Patient;

@Repository
@RequiredArgsConstructor
public class PatientRepository {

    public final EntityManager em;

    //환자 등록
    public void save(Patient patient) {
        em.persist(patient);
    }

    //환자 조회
    public Patient findById(Long id) {
        return em.find(Patient.class, id);
    }

}