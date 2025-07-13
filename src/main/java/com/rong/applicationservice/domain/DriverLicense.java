package com.rong.applicationservice.domain;

import lombok.Data;

import java.time.LocalDate;

@Data
public class DriverLicense {
    private String licenseNumber;
    private LocalDate driverLicenseExpiration;
    private String licenseDoc;
}
