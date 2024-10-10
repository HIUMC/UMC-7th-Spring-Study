package hello.hello_spring.service;

import hello.hello_spring.domain.Member;
import hello.hello_spring.repository.MemoryMemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class MemberServiceTest {

    MemberService memberService;
    MemoryMemberRepository memberRepository;

    @BeforeEach //시작하기전 각각 실행 - 테스트는 독립적으로 돼야해서
    public void beforeEach() {
        memberRepository = new MemoryMemberRepository();
        memberService=new MemberService(memberRepository);
    }

    @AfterEach //메모리 클리어
    public void afterEach() {
        memberRepository.clearStore();
    }

    @Test
    void 회원가입() { //테스트 코드는 이름 한글 가능
        //given
        Member member = new Member();
        member.setName("hello");

        //when
        Long saveId = memberService.join(member); //검증하는 것

        //then 잘 저장 되었는지
        Member findMember = memberService.findOne(saveId).get();
        Assertions.assertThat(member.getName()).isEqualTo(findMember.getName());
    }

    @Test
    public void 중복_회원_예외(){
        //given
        Member member1 = new Member();
        member1.setName("spring");

        Member member2 = new Member();
        member2.setName("spring");

        //when
        memberService.join(member1);

        //member2를 넣으면(뒷부분) 앞 부분의 예외(IllegalStateException 클래스)가 발생해야 함
        IllegalStateException e = assertThrows(IllegalStateException.class, ()-> memberService.join(member2));
        Assertions.assertThat(e.getMessage()).isEqualTo(("이미 존재하는 회원입니다."));
        /*
        try {
            memberService.join(member2);
            //맨 위 줄 실행하면 반드시 exception(catch)으로 내려가야함
            fail();
        } catch (IllegalStateException e){
            Assertions.assertThat(e.getMessage()).isEqualTo("이미 존재하는 회원입니다.");
        }*/

        //then
    }

    @Test
    void findMembers() {
    }

    @Test
    void findOne() {
    }
}