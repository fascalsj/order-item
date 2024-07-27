package com.obs.recruitment.order.item.shared.exception.handler;

import com.obs.recruitment.order.item.shared.dto.response.GlobalResponse;
import com.obs.recruitment.order.item.shared.exception.DataNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(DataNotFoundException.class)
    public ResponseEntity<GlobalResponse<Object>> handleCustomNotFoundException(
            DataNotFoundException dataNotFoundException) {
        String message = dataNotFoundException.getMessage();
        Integer status = dataNotFoundException.getStatus();
        return ResponseEntity.status(status)
                .body(GlobalResponse.builder()
                        .message(message)
                        .code(status)
                        .build());
    }
}
