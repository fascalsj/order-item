package com.obs.recruitment.inventory.shared.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class GlobalResponse<T> {
    private Integer code;
    private String message;
    private T result;
}
