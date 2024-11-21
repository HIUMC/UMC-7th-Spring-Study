package umc.spring.apiPayload.code;

import java.awt.desktop.UserSessionEvent;
import java.awt.desktop.UserSessionEvent.Reason;

public interface BaseCode {

    ReasonDTO getReason();

    ReasonDTO getReasonHttpStatus();
}
