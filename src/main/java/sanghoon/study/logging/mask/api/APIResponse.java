package sanghoon.study.logging.mask.api;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import sanghoon.study.logging.mask.api.service.response.PayloadResponse;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class APIResponse<T> {
    private int code;
    private HttpStatus status;
    private String message;
    private T data;

    public APIResponse(HttpStatus status,String message,T data){
        this.code = status.value();
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public static <T> APIResponse<T> of(HttpStatus httpStatus,String message,T data){
        return new APIResponse<>(httpStatus, message, data);
    }

    public static <T> APIResponse<T> of(HttpStatus httpStatus,T data){
        return of(httpStatus, httpStatus.name(), data);
    }

    public static <T> APIResponse<T> ok(T data){
        return of(HttpStatus.OK, data);
    }
}
