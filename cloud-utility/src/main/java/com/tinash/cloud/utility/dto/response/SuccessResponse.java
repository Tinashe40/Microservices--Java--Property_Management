package com.tinash.cloud.utility.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SuccessResponse<T> extends ApiResponse<T> {

    public SuccessResponse(T data) {
        super(data);
        this.setMessage("Success");
    }
}
