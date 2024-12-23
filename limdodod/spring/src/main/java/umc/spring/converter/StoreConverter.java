package umc.spring.converter;

import org.springframework.stereotype.Component;
import umc.spring.domain.Region;
import umc.spring.domain.Store;
import umc.spring.web.dto.StoreRequestDTO;
import umc.spring.web.dto.StoreResponseDTO;

@Component
public class StoreConverter {

    public static Store toEntity(StoreRequestDTO.JoinDto request, Region region) {
        Store store = new Store();
        store.setId(Long.valueOf(request.getId()));
        store.setName(request.getName());
        store.setAddress(request.getAddress());
        store.setScore(request.getScore());
        store.setRegion(region);
        return store;
    }

    public static StoreResponseDTO.JoinResponseDTO toJoinResponseDto(Store store) {
        return StoreResponseDTO.JoinResponseDTO.builder()
                .id(String.valueOf(store.getId()))
                .name(store.getName())
                .address(store.getAddress())
                .score(store.getScore())
                .message("Store successfully added!")
                .build();
    }
}
