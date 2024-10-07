package hello.hellospring.service;

import hello.hellospring.domain.Member;
import hello.hellospring.repository.MemberRepository;
//import hello.hellospring.repository.MemoryMemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional
public class MemberService {


    private final MemberRepository memberRepository ;

    public MemberService(MemberRepository memberRepository){
        this.memberRepository = memberRepository;
    }
    /*
    회원가입

    */

    public Long join(Member member){

//        long start = System.currentTimeMillis();
//
//        try{
//            validateDuplicateMember(member);
//            memberRepository.save(member);
//            return member.getId();
//        }  finally {
//            long finish = System.currentTimeMillis();
//            long timeMs = finish - start;
//            System.out.println("join = " + timeMs + "ms");
//        }
        
        //같은 이름이 있는 경우 안된다고 하자
        validateDuplicateMember(member);

        memberRepository.save(member);
        return member.getId();
    }

    private void validateDuplicateMember(Member member) {
        memberRepository.findByName(member.getName())
                .ifPresent(m->{
            throw new IllegalStateException(("이미 존재하는 회원입니다."));
        });
    }

    public List<Member> findMembers(){
        return memberRepository.findAll();

    }

    public Optional<Member> findOne(Long memberId){
        return memberRepository.findById(memberId);
    }
}
