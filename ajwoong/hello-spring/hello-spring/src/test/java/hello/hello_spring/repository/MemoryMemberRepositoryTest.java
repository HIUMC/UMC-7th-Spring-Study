package hello.hello_spring.repository;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import hello.hello_spring.domain.Member;

import java.util.List;

class MemoryMemberRepositoryTest {
    MemoryMemberRepository repository = new MemoryMemberRepository();

    @AfterEach
    public void afterEach(){
        repository.clearStore();
    }

    @Test
    public void save(){
        Member member = new Member();
        member.setName("Spring");

        repository.save(member);

        Member result = repository.findById(member.getId()).get();
        assertThat(member).isEqualTo(result);
        //Assertions.assertEquals(member, result);
//        System.out.println("result = " + (result == member));
    }

    @Test
    public void findByName(){
        Member member1 = new Member();
        member1.setName("Spring1");
        repository.save(member1);

        Member member2 = new Member();
        member2.setName("Spring2");
        repository.save(member2);

        Member result =repository.findByName("Spring1").get();

        assertThat(member1).isEqualTo(result);

    }

    @Test
    public void findAll(){
        Member member1 = new Member();
        member1.setName("Spring1");
        repository.save(member1);

        Member member2 = new Member();
        member2.setName("Spring2");
        repository.save(member2);

        Member member3 = new Member();
        member3.setName("Spring3");
        repository.save(member3);

        List<Member> members = repository.findAll();

        assertThat(members.size()).isEqualTo(3);

    }

//    @Test
//    public void findById(){
//        Member member = new Member();
//        member.setName("Spring1");
//        repository.save(member);
//
//
//
//        Member result = repository.findById(member.getId()).get();
//        assertThat(result).isEqualTo(member);
//    }


}
