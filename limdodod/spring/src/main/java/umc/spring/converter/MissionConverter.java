package umc.spring.converter;

import org.springframework.stereotype.Component;
import umc.spring.domain.Mission;
import umc.spring.domain.Store;
import umc.spring.web.dto.MissionRequestDTO;
import umc.spring.web.dto.MissionResponseDTO;

@Component
public class MissionConverter {

    public static Mission toEntity(MissionRequestDTO.AddMissionDto request, Store store) {
        return Mission.create(
                store,
                request.getMissionSpec(),
                request.getReward()
        );
    }

    public static MissionResponseDTO.MissionResultDto toDto(Mission mission) {
        return new MissionResponseDTO.MissionResultDto(
                mission.getId(),
                mission.getStore().getName(),
                mission.getMissionSpec(),
                mission.getReward()
        );
    }
}
