package toyproject.hongikhospital.repository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import toyproject.hongikhospital.domain.Reservation;

@Repository
@RequiredArgsConstructor
public class ReserveRepository {

    public final EntityManager em;

    //예약 생성
    public void save(Reservation reservation) {
        em.persist(reservation);
    }

    //예약 조회
    public Reservation findById(Long id) {
        return em.find(Reservation.class, id);
    }

}
