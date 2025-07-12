package com.rong.applicationservice.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResponseFail extends GeneralResponse{
    private boolean success;
    private LocalDateTime time;
    private int status;
    private String error;
    private String message;
}
