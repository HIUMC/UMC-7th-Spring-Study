package umc.study.converter;

import org.springframework.stereotype.Component;
import umc.study.domain.Region;
import umc.study.domain.Store;
import umc.study.web.dto.StoreRequestDTO;
import umc.study.web.dto.StoreResponseDTO;

@Component
public class StoreConverter {

    public static Store toEntity(StoreRequestDTO.JoinDto request, Region region) {
        Store store = new Store();
        store.setId(request.getId());
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
