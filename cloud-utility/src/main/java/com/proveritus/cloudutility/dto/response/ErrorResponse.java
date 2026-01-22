package com.proveritus.cloudutility.dto.response;

import com.tinash.cloud.utility.dto.response.ApiResponse;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorResponse extends ApiResponse<Object> {

    public ErrorResponse(String message) {
        this.setMessage(message);
    }
}