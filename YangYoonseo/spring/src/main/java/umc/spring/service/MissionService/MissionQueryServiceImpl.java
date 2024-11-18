package umc.spring.service.MissionService;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import umc.spring.domain.Mission;
import umc.spring.repository.MissionRepository.MissionRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MissionQueryServiceImpl implements MissionQueryService {
    private final MissionRepository missionRepository;

    @Override
    public List<Mission> getMissions(String region){
        return missionRepository.findMissionByRegion(region);
    }
}
