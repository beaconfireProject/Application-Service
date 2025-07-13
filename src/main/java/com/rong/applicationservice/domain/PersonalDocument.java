package com.rong.applicationservice.domain;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class PersonalDocument {
//    private String id;
    private String path;
    private String title;
    private String comment;
    private LocalDate createDate;
}
