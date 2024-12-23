package umc.spring.web.dto;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;

import java.time.LocalDate;

public class MissionRequestDTO {

    @Getter
    public static class AddMissionDto {
        @NotBlank
        private String storeId; // 미션을 추가할 가게 ID

        @NotBlank
        private String missionSpec; // 미션 설명

        @NotNull
        private LocalDate deadline;

        @Positive
        private int reward;

    }
}
