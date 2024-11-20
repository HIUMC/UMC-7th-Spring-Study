package umc.study.web.dto;

public class ResponseDto<T> {
    private T data;
    private String message;
    private int status;

    public static <T> ResponseDto<T> onSuccess(T data, int status) {
        return new ResponseDto<>();
    }

    public static <T> ResponseDto<T> onError(String message, int status) {
        return new ResponseDto<>();
    }
}
