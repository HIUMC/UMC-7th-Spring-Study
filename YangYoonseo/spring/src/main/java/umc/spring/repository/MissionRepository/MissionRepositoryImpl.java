package umc.spring.repository.MissionRepository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import umc.spring.domain.Mission;
import umc.spring.domain.QMission;
import umc.spring.domain.QRegion;
import umc.spring.domain.QStore;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class MissionRepositoryImpl implements MissionRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;
    private final QMission mission = QMission.mission;
    private final QRegion region = QRegion.region;
    private final QStore store = QStore.store;

    @Override
    public List<Mission> findMissionByRegion(String regionName){
        BooleanBuilder predicate = new BooleanBuilder();

        if(region != null){
            predicate.and(region.name.eq(regionName));
        }
//        predicate.and(mission.deadline.gt(java.time.LocalDateTime.now()));

        return jpaQueryFactory.selectFrom(mission)
                .innerJoin(mission.store, store)
                .innerJoin(store.region, region)
                .where(predicate)
                .fetch();









    }
}
