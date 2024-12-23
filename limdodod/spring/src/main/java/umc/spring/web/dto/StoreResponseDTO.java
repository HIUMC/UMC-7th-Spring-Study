package umc.spring.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class StoreResponseDTO {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class JoinResponseDTO {
        private String id;
        private String name;                // 가게 이름
        private String address;             // 가게 주소
        private Float score;                // 가게 평점
        private String message;             // 처리 결과 메시지

    }
}
