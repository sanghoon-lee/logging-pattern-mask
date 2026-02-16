package sanghoon.study.logging.mask.api.service.request;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PayloadRequest {
    @NotNull
    private String message;

    @Builder
    public PayloadRequest(String message){
        this.message = message;
    }
}
