package com.rong.applicationservice.domain;

import lombok.Data;

@Data
public class Address {
//    private String id;
    private String addressLine1;
    private String addressLine2;
    private String city;
    private String state;
    private String zipCode;
}

