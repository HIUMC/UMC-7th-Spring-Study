package jpabook.jpashop.service;

import jakarta.transaction.Transactional;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@Transactional
@RunWith(SpringRunner.class)
@SpringBootTest //Autowired 같은거 제공
public class MemberServiceTest {

    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;

    //회원 가입
    @Test
    public void 회원가입() throws Exception {
        Member  member = new Member();
        member.setName("zoro");

        Long savedId = memberService.join(member);

        assertEquals(member,memberRepository.findOne(savedId));

    }

    //중복회원 예외
    @Test(expected = IllegalStateException.class)
    public void 중복_회원_예외() throws Exception {
        Member member = new Member();
        member.setName("kim");
        Member member2 = new Member();
        member2.setName("kim");

        memberService.join(member);
        memberService.join(member2);


    }

}