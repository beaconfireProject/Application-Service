package com.rong.applicationservice.Controller;

import com.rong.applicationservice.dto.request.RegistrationRequest;
import com.rong.applicationservice.dto.response.RegistrationDataResponse;
import com.rong.applicationservice.dto.response.ResponseSuccess;
import com.rong.applicationservice.service.RegistrationService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api")
public class RegistrationController {


    private final RegistrationService registrationService;

    public RegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @PostMapping("/register")
    public ResponseSuccess hello(@RequestBody RegistrationRequest registrationRequest) {

        int id = registrationService.addUser(registrationRequest);

        return ResponseSuccess.builder()
                .success(true)
                .time(LocalDateTime.now())
                .data(RegistrationDataResponse.builder().id(id).build())
                .message("register successfully")
                .build();
    }

}
