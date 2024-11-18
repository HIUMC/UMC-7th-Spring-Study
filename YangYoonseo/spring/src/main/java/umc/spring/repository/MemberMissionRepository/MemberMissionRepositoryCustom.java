package umc.spring.repository.MemberMissionRepository;

import umc.spring.domain.Store;
import umc.spring.domain.mapping.MemberMission;

import java.util.List;

public interface MemberMissionRepositoryCustom {

    List<MemberMission> dynamicQueryWithBooleanBuilder(Long memberId);
}
