package umc.study.web.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import umc.study.apiPayload.ApiResponse;
import umc.study.converter.StoreConverter;
import umc.study.domain.Store;
import umc.study.service.StoreService.StoreCommandService;
import umc.study.web.dto.StoreRequestDTO;
import umc.study.web.dto.StoreResponseDTO;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/stores")
public class StoreRestController {
    private final StoreCommandService storeCommandService;

    @PostMapping("/")
    public ApiResponse<StoreResponseDTO.JoinResponseDTO> join(@RequestBody @Valid StoreRequestDTO.JoinDto request) {
        Store store = storeCommandService.joinStore(request);
        return ApiResponse.onSuccess(StoreConverter.toJoinResponseDto(store));
    }
}