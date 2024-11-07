package umc7th.study.domain.mapping;
import jakarta.persistence.*;
import lombok.*;
import umc7th.study.domain.Member;
import umc7th.study.domain.Mission;
import umc7th.study.domain.Store;
import umc7th.study.domain.Terms;
import umc7th.study.domain.common.BaseEntity;
import umc7th.study.domain.enums.MissionStatus;


@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class MemberMission extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private MissionStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mission_id")
    private Mission mission;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;
}
