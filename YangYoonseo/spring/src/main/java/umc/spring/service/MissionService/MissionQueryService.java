package umc.spring.service.MissionService;

import umc.spring.domain.Mission;

import java.util.List;

public interface MissionQueryService {
    List<Mission> getMissions(String region);

}
