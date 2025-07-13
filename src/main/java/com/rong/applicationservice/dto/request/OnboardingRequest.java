package com.rong.applicationservice.dto.request;

import com.rong.applicationservice.domain.Address;
import com.rong.applicationservice.domain.Contact;
import com.rong.applicationservice.domain.DriverLicense;
import com.rong.applicationservice.domain.VisaStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class OnboardingRequest {
    private String firstName;
    private String lastName;
    private String middleName;
    private String preferredName;

    private String avatar;

    private Address address;

    private String cellPhone;
    private String workPhone;

    private String email;

    private String gender;
    private String ssn;
    private LocalDate dob;

    private LocalDate startDate;
    private LocalDate endDate;
    private VisaStatus visaStatus;
    private String workDoc;

    private DriverLicense driverLicense;
    private List<Contact> contact;
}
