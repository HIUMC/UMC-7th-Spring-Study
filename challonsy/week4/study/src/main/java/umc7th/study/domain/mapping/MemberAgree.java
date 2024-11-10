package umc7th.study.domain.mapping;

import jakarta.persistence.*;
import lombok.*;
import umc7th.study.domain.Member;
import umc7th.study.domain.Terms;
import umc7th.study.domain.common.BaseEntity;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class MemberAgree extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "terms_id")
    private Terms terms;
}
