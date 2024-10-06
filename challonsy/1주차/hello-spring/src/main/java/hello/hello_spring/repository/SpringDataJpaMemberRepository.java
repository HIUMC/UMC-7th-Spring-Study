package hello.hello_spring.repository;

import hello.hello_spring.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SpringDataJpaMemberRepository extends JpaRepository<Member, Long>,MemberRepository {

    /**
     *스프링 데이터 JPA가 JpaRepository 상속하고 있는 인터페이스를 보면 자동으로 메서드들을 만들어줌
     */
    @Override
    Optional<Member> findByName(String name);
}
