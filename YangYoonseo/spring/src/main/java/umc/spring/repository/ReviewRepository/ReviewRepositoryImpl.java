package umc.spring.repository.ReviewRepository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import umc.spring.domain.Member;
import umc.spring.domain.QReview;
import umc.spring.domain.Review;
import umc.spring.domain.Store;
import umc.spring.repository.MemberRepository.MemberRepository;
import umc.spring.repository.StoreRepository.StoreRepository;

@Repository
@RequiredArgsConstructor
public class ReviewRepositoryImpl implements ReviewRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;
    private final QReview review = QReview.review;
    private final MemberRepository memberRepository;
    private final StoreRepository storeRepository;

    @Override
    public Review createReview(Long memberId, Long storeId, String body, float score){
        // memberId로 Member 객체 찾기
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid member ID"));

        // storeId로 Store 객체 찾기
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid store ID"));


        Review newReview = Review.builder()
                .member(member)
                .store(store)
                .body(body)
                .score(score)
                .build();


        return jpaQueryFactory.selectFrom(review)
                .where(review.member.eq(member)
                        .and(review.store.eq(store)))
                .fetchFirst();  // 예시로 fetchFirst로 데이터를 반환



    }

}
