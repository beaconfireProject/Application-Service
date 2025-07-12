package com.rong.applicationservice.dto.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Getter
@Setter
public class RegistrationRequest {
    @NotEmpty
    private String username;
    @NotEmpty
    @Size(min = 5, max = 12)
    private String password;
    @Email
    @NotEmpty
    private String email;
}
