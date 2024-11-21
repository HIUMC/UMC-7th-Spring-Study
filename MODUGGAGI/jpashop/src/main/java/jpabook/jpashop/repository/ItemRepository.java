package jpabook.jpashop.repository;

import jakarta.persistence.EntityManager;
import jpabook.jpashop.domain.item.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ItemRepository {

    private final EntityManager em;

    public void save(Item item) {
        if (item.getId() == null) {
            em.persist(item);
        } else {
            em.merge(item); //merge의 기능?? -> 준영속 상태의 엔티티를 영속 상태로 바꾼다 update
            //1. 준영속 엔티티의 식별자 값으로 영속 엔티티를 조회한다.
            //2. 영속 엔티티의 값을 준영속 엔티티의 값으로 모두 교체한다.(병합한다.)
            // 이때 파라미터로 넘어간 item 객체는 영속 상태가 되지는 않는다.. em.merge(item)의 반환된 객체가 영속상태인 것
            // pr 위한 수정
        }
    }

    public Item findOne(Long id) {
        return em.find(Item.class, id);
    }

    public List<Item> findAll() {
        return em.createQuery("select i from Item i", Item.class)
                .getResultList();
    }
}
