package hello.hello_spring.domain;

// ❓ 스프링이 Member의 게터에 접근하는 원리가 뭐지

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Member {

    // ID를 DB가 알아서(내가 볼땐 아무 숫자) 생성해주는 전략 = identity 전략
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
