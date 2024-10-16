package hello.hello_spring.repository;

import hello.hello_spring.domain.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

class MemoryMemberRepositoryTest {

    MemoryMemberRepository repository = new MemoryMemberRepository();

    // ⭐ test 마다 끝나면 지워주는 기능 (한번에(클래스 단위) 테스트해도 서로 연관 안되고 에러 안뜨게)
    @AfterEach
    public void afterEach() {
        repository.clearStore();
    }

    @Test
    public void save() {
        Member member = new Member();
        member.setName("spring");

        repository.save(member);

        Member result = repository.findById(member.getId()).get();

//      System.out.println("result = " + (result == member)); // 위처럼 문자로 확인할 수 있지만,
//      Assertions.assertEquals(result, member); // junit 제공 Assertions 기능 : 순서 가독성 떨어짐
        assertThat(member).isEqualTo(result); // assertj 제공 기능 : 가독성, 에러 상술 / 'static import'로 들어가서 assertThat 바로 칠 수 있음.
    }

    @Test
    public void findByName() {
        Member member1 = new Member();
        member1.setName("spring 1");
        repository.save(member1);

        Member member2 = new Member(); // 리팩토링 Shift + F6
        member2.setName("spring 2");
        repository.save(member2);

        Member result = repository.findByName("spring 1").get();

        assertThat(result).isEqualTo(member1);
    }

    @Test
    public void findAll() {
        Member member1 = new Member();
        member1.setName("spring 1");
        repository.save(member1);

        Member member2 = new Member();
        member2.setName("spring 2");
        repository.save(member2);

        List<Member> result = repository.findAll();

        assertThat(result.size()).isEqualTo(2);
    }
}
