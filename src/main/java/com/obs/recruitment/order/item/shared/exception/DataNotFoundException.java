package com.obs.recruitment.order.item.shared.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

@Slf4j
public class DataNotFoundException extends RuntimeException {
    private Integer status;
    private String message;

    public DataNotFoundException(String message) {
        super(HttpStatus.NOT_FOUND.getReasonPhrase());
        this.status = HttpStatus.NOT_FOUND.value();
        this.message = message;
        log.info(message);
    }
}
