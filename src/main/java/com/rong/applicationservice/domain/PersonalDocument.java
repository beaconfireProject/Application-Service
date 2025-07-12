package com.rong.applicationservice.domain;

import lombok.Data;

import java.time.LocalDate;

@Data
public class PersonalDocument {
//    private String id;
    private String path;
    private String title;
    private String comment;
    private LocalDate createDate;
}
