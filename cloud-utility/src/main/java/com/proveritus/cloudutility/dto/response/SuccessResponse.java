package com.proveritus.cloudutility.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SuccessResponse<T> extends ApiResponse<T> {

    public SuccessResponse(T data) {
        this.setData(data);
        this.setMessage("Success");
    }
}