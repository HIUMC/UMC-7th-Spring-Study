package umc.spring.repository.MemberMissionRepository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import umc.spring.domain.QMission;
import umc.spring.domain.QStore;
import umc.spring.domain.Store;
import umc.spring.domain.enums.MissionStatus;
import umc.spring.domain.mapping.MemberMission;
import umc.spring.domain.mapping.QMemberMission;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class MemberMissionRepositoryImpl implements MemberMissionRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;
    private final QMemberMission memberMission = QMemberMission.memberMission;

    @Override
    public List<MemberMission> dynamicQueryWithBooleanBuilder(Long memberId) {
//        진행중
        BooleanBuilder ongoingPredicate = new BooleanBuilder();
        ongoingPredicate.and(memberMission.status.eq(MissionStatus.CHALLENGING));

//        완료
        BooleanBuilder endPredicate = new BooleanBuilder();
        endPredicate.and(memberMission.status.eq(MissionStatus.COMPLETE));

        if(memberId != null){
            ongoingPredicate.and(memberMission.member.id.eq(memberId));
            endPredicate.and(memberMission.member.id.eq(memberId));

        }

//        진행중
        List<MemberMission> ongoingMissions = jpaQueryFactory.selectFrom(memberMission).where(ongoingPredicate).fetch();

//        완료
        List<MemberMission> endMissions = jpaQueryFactory.selectFrom(memberMission).where(endPredicate).fetch();


        ongoingMissions.addAll(endMissions);
        return ongoingMissions;
    }

}
