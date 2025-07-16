package com.rong.applicationservice.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class TimeRequest {
    private LocalDateTime lastModificationDate;
}
