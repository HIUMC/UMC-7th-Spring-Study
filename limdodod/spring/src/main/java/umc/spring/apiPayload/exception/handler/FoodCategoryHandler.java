package umc.spring.apiPayload.exception.handler;

import umc.spring.apiPayload.code.BaseErrorCode;

public class FoodCategoryHandler extends RuntimeException {

  private final BaseErrorCode errorCode;

  public FoodCategoryHandler(BaseErrorCode errorCode) {
    super(errorCode.getReason().getMessage());
    this.errorCode = errorCode;
  }

  public BaseErrorCode getErrorCode() {
    return errorCode; // 추가적인 정보 제공
  }
}
