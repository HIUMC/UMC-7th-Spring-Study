package umc.spring.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import umc.spring.apiPayload.ApiResponse;
import umc.spring.converter.StoreConverter;
import umc.spring.domain.Store;
import umc.spring.service.StoreService.StoreCommandService;
import umc.spring.web.dto.StoreRequestDTO;
import umc.spring.web.dto.StoreResponseDTO;

@RestController
    @RequiredArgsConstructor
    @RequestMapping("/stores")
    public class StoreRestController {

        private final StoreCommandService storeCommandService;

        @PostMapping("/")
        public ApiResponse<StoreResponseDTO.JoinResponseDTO> addStore(@RequestBody @Valid StoreRequestDTO.JoinDto request) {
            Store store = storeCommandService.addStore(request);
            return ApiResponse.onSuccess(StoreConverter.toJoinResponseDto(store));
        }
        }