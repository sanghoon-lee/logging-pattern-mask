package sanghoon.study.logging.mask.api.service;

import org.springframework.stereotype.Service;
import sanghoon.study.logging.mask.api.service.request.PayloadRequest;
import sanghoon.study.logging.mask.api.service.response.PayloadResponse;

import java.time.LocalTime;

@Service
public class PayloadService {
    public PayloadResponse publish(PayloadRequest request){
        return PayloadResponse.builder()
                .created(LocalTime.now())
                .message(request.getMessage())
                .build();
    }
}
