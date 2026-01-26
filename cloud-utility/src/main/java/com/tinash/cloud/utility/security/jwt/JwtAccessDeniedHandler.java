package com.tinash.cloud.utility.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tinash.cloud.utility.exception.ApiError;
import jakarta.servlet.http.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        ApiError apiError = new ApiError(HttpStatus.FORBIDDEN);
        apiError.setMessage(accessDeniedException.getMessage());
        apiError.setDebugMessage(accessDeniedException.getLocalizedMessage());

        objectMapper.writeValue(response.getOutputStream(), apiError);
    }
}
