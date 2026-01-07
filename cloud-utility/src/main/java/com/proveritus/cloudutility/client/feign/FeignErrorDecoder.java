package com.proveritus.cloudutility.client.feign;

import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.stereotype.Component;

@Component
public class FeignErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String methodKey, Response response) {
        // Implement custom error decoding logic here
        return new Exception("Feign error");
    }
}