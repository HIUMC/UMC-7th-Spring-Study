package umc.spring.web.dto;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import umc.spring.domain.common.BaseEntity;
import umc.spring.validation.annotation.ExistCategories;

import java.util.List;

public class StoreRequestDTO {

    @Getter
    public static class JoinDto{
        @NotBlank
        String id;
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
