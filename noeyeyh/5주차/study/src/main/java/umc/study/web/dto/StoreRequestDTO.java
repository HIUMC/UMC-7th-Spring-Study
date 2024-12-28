package umc.study.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import java.util.List;

public class StoreRequestDTO {
    @Getter

    public static class JoinDto{
        @NotBlank
        Long id;

        @NotBlank
        String name;

        @NotNull
        private Long regionId;

        @Size(min = 5, max = 12)
        String address;

        @Positive
        Float score;
    }
}
