package com.tinash.cloud.utility.exception.technical;

import com.tinash.cloud.utility.exception.base.TechnicalException;
import com.tinash.cloud.utility.enums.ErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception for errors occurring during JSON processing (serialization or deserialization).
 * This is a technical exception indicating an issue with data format or processing.
 */
@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class JsonProcessingTechnicalException extends TechnicalException {

    public JsonProcessingTechnicalException(String message) {
        super(message, ErrorCode.JSON_PROCESSING_ERROR.name());
    }

    public JsonProcessingTechnicalException(String message, Throwable cause) {
        super(message, ErrorCode.JSON_PROCESSING_ERROR.name(), cause);
    }
}
