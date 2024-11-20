package umc.study.apiPayload.code;

public interface BaseErrorCode {

    ErrorReason getReason();

    ErrorReason getReasonHttpStatus();
}
