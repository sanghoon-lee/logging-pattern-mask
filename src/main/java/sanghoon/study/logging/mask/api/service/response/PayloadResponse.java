package sanghoon.study.logging.mask.api.service.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PayloadResponse {
    private String message;
    private LocalTime created;

    @Builder
    public PayloadResponse(String message,LocalTime created){
        this.message = message;
        this.created = created;
    }
}

