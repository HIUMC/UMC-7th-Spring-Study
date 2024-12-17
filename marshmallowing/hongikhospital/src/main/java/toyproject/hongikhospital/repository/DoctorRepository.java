package toyproject.hongikhospital.repository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import toyproject.hongikhospital.domain.Doctor;

@Repository
@RequiredArgsConstructor
public class DoctorRepository {

    public final EntityManager em;

    //의사 등록
    public void save(Doctor doctor) {
        em.persist(doctor);
    }

    //의사 조회
    public Doctor findById(Long id) {
        return em.find(Doctor.class, id);
    }
}
