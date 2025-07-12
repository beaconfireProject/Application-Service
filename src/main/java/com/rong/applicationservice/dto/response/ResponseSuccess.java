package com.rong.applicationservice.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResponseSuccess extends GeneralResponse{
    private boolean success;
    private LocalDateTime time;
    private Object data;
    private String message;
}
