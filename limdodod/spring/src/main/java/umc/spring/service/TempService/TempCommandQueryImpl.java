package umc.spring.service.TempService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor

public abstract class TempCommandQueryImpl implements TempQueryService{

    @Override
    public void checkFlag(Integer flag) {

    }
}
