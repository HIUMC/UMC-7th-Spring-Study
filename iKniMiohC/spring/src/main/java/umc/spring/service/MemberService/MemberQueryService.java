package umc.spring.service.MemberService;

import umc.spring.domain.Member;

import java.util.Optional;

public interface MemberQueryService {

    Optional<Member> findMember(Long id);

}