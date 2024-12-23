package umc.spring.web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class MissionResponseDTO {
    @Getter
    @AllArgsConstructor
    public static class MissionResultDto {
        private Long missionId;
        private String storeName; // 가게 이름
        private String missionSpec;
        private int reward; // 보상 포인트
    }
}
