package com.rong.applicationservice.Controller;

import com.rong.applicationservice.domain.Employee;
import com.rong.applicationservice.dto.response.ApplicationDataResponse;
import com.rong.applicationservice.dto.response.GeneralResponse;
import com.rong.applicationservice.dto.response.ResponseSuccess;
import com.rong.applicationservice.service.ApplicationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/application")
public class ApplicationController {

    private final ApplicationService applicationService;

    public ApplicationController(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    @PostMapping("/onboarding/")
    public GeneralResponse createOnboardingApplication(@RequestBody Employee employee) {
        int id = applicationService.createOnboardingApplication(employee);
        return ResponseSuccess.builder()
                .success(true)
                .time(LocalDateTime.now())
                .data(ApplicationDataResponse.builder().id(id).build())
                .message("Create application successfully").build();
    }

    @GetMapping("/employee")
    public ResponseEntity<List<Employee>> getAllEmployee() {
        return applicationService.getAllEmployee();
    }

}
