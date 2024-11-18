package umc.spring.repository.ReviewRepository;

import umc.spring.domain.Review;

import java.util.List;

public interface ReviewRepositoryCustom {
    Review createReview(Long memberId, Long StoreId, String body, float score);
}
