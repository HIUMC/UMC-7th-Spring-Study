package umc.study.service.StoreService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import umc.study.converter.StoreConverter;
import umc.study.domain.Region;
import umc.study.domain.Review;
import umc.study.domain.Store;
import umc.study.repository.RegionRepository;
import umc.study.repository.StoreRepository.StoreRepository;
import umc.study.web.dto.StoreRequestDTO;

@Service
@RequiredArgsConstructor
public class StoreCommandService {

    private final StoreRepository storeRepository;
    private final RegionRepository regionRepository;

    public Store joinStore(StoreRequestDTO.JoinDto request) {
        Region region = regionRepository.findById(request.getRegionId()).orElseThrow(() -> new IllegalArgumentException("Invalid region ID: " + request.getRegionId())); // 예외 처리 추가
        Store store = StoreConverter.toEntity(request, region);
        return storeRepository.save(store);
    }

}
