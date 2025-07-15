package com.rong.applicationservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DtoResponse<T> {
    private boolean success;
    private LocalDateTime timestamp;
    private T data;
    private String message;
}
