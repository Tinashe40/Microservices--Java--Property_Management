package com.tinash.cloud.utility.client;

import com.tinash.cloud.utility.exception.base.InternalServerErrorException;

import com.tinash.cloud.utility.exception.technical.ResourceNotFoundException;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.http.HttpStatus;

/**
 * Custom error decoder for Feign clients.
 * It translates HTTP error responses into specific exceptions,
 * providing more granular error handling than generic FeignException.
 */
public class CustomErrorDecoder implements ErrorDecoder {

    private final ErrorDecoder defaultErrorDecoder = new Default();

    @Override
    public Exception decode(String methodKey, Response response) {
        HttpStatus responseStatus = HttpStatus.valueOf(response.status());

        if (HttpStatus.NOT_FOUND.equals(responseStatus)) {
            // Can parse the response body to get more specific error message if needed
            return new ResourceNotFoundException(
                    String.format("Resource not found when calling service: %s, method: %s", response.request().url(), methodKey)
            );
        } else if (HttpStatus.INTERNAL_SERVER_ERROR.equals(responseStatus)) {
            return new InternalServerErrorException(
                    String.format("Internal server error when calling service: %s, method: %s", response.request().url(), methodKey)
            );
        }
        // Let default Feign error decoder handle other cases
        return defaultErrorDecoder.decode(methodKey, response);
    }
}
