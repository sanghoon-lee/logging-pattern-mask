package sanghoon.study.logging.mask.api.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import sanghoon.study.logging.mask.api.APIResponse;
import sanghoon.study.logging.mask.api.service.PayloadService;
import sanghoon.study.logging.mask.api.service.request.PayloadRequest;
import sanghoon.study.logging.mask.api.service.response.PayloadResponse;

@Slf4j
@RequiredArgsConstructor
@RestController
public class PayloadController {
    private final PayloadService payloadService;

    @PostMapping("/api/payload")
    public APIResponse<PayloadResponse> publish(@Valid @RequestBody PayloadRequest request){
        PayloadResponse payloadResponse = payloadService.publish(request);

        log.info("received : {}",request.getMessage());

        return APIResponse.ok(payloadResponse);
    }
}
