package umc.study.domain;

import jakarta.persistence.*;
import lombok.*;
import umc.study.domain.common.BaseEntity;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Review extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 20)
    private String title;

    @Column(nullable = false, precision = 5, scale = 2)
    private Float score;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id") // 외래 키 설정
    private Member member; // Member 필드 추가
}
