package umc.spring.service.TempService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import umc.spring.apiPayload.code.status.ErrorStatus;
import umc.spring.apiPayload.exception.handler.TempHandler;

@RequiredArgsConstructor
@Service
public class TempQueryServiceImpl implements TempQueryService{

    @Override
    public void checkFlag(Integer flag) {
        if (flag==1)
            throw new TempHandler(ErrorStatus.TEMP_EXCEPTION);
    }

}