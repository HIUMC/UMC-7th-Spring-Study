package hello.hello_spring.service;

import hello.hello_spring.domain.Member;
import hello.hello_spring.repository.MemberRepository;
import hello.hello_spring.repository.MemoryMemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional
public class MemberService {
    private final MemberRepository memberRepository;


    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    /**
    *  회원 가입
     */

    public Long join(Member member) {


            validtatedDuplicateMember(member);
            memberRepository.save(member);
          return member.getId();

        //간츤 이름이 있는 중복 회원 불가

        // == 검증 코드 == //
       /* Optional<Member> result = memberRepository.findByName(member.getName());
        result.ifPresent(m -> {
            throw new IllegalStateException("이미 존재하는 회원 입니다");
        });*/
        //위 코드를 더 예쁘게 바꾸기가 가능!

       /* memberRepository.findByName(member.getName())
                        .isPresent(m->{
                            throw new IllegalStateException("이미 존재하는 회원 입니다");
                        });*/
        // 이렇게! 근데 아예 메서드 추출을 해버리자

        //validtatedDuplicateMember(member);
        //이렇게


       // memberRepository.save(member);
        //return member.getId();

    }

    private void validtatedDuplicateMember(Member member) {
        memberRepository.findByName(member.getName())
                        .ifPresent(m->{
                            throw new IllegalStateException("이미 존재하는 회원입니다");
                        });
    }

    /**
     *  전체 회원 조회
     */

    public List<Member> findMembers(){
        //return memberRepository.findAll();
            return memberRepository.findAll();

    }

    public Optional<Member> findOne(Long memberId){
        return memberRepository.findById(memberId);
    }
}
