package com.rong.applicationservice.domain;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class VisaStatus {
//    private String id;
    private String visaType;
    private Boolean activeFlag;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDate lastModificationDate;
}

