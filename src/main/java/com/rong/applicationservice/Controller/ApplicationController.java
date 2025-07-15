package com.rong.applicationservice.Controller;

import com.rong.applicationservice.domain.ApplicationWorkFlow;
import com.rong.applicationservice.domain.DigitalDocument;
import com.rong.applicationservice.domain.Employee;
import com.rong.applicationservice.domain.Status;
import com.rong.applicationservice.dto.request.Comment;
import com.rong.applicationservice.dto.request.OnboardingRequest;
import com.rong.applicationservice.dto.response.*;
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

    @PostMapping("/onboarding")
    public GeneralResponse createOnboardingApplication(@RequestBody OnboardingRequest onboardingRequest) {
        int id = applicationService.createOnboardingApplication(onboardingRequest);
        return ResponseSuccess.builder()
                .success(true)
                .time(LocalDateTime.now())
                .data(ApplicationDataResponse.builder().id(id).build())
                .message("Create application successfully").build();
    }

    @GetMapping("/employee")
    public ResponseEntity<DtoResponse> getAllEmployee() {
        return applicationService.getAllEmployee();
    }

    @GetMapping("/onboarding/{userId}")
    public ResponseEntity<DtoResponse> getEmployeeById(@PathVariable String userId) {
        return applicationService.getEmployeeById(userId);
    }

    @GetMapping("/onboarding/status/{userId}")
    public GeneralResponse checkStatus(@PathVariable String userId) {
        Status status = applicationService.getOnboardingStatusById(userId);
        return ResponseSuccess.builder()
                .success(true)
                .time(LocalDateTime.now())
                .data(status)
                .message("get status by id successfully").build();
    }

    @GetMapping("/onboarding/applications")
    public GeneralResponse getAllOngoingApplications() {
        List<ApplicationWorkFlow> applicationWorkFlowList = applicationService.getAllOngoingApplications();
        return ResponseSuccess.builder()
                .success(true)
                .time(LocalDateTime.now())
                .data(applicationWorkFlowList)
                .message("get all ongoing applications successfully")
                .build();
    }

    @GetMapping("/onboarding/application/{applicationId}")
    public GeneralResponse getApplicationDataResponse(@PathVariable int applicationId) {
        ApplicationDetailResponse applicationDetail = applicationService.getOngoingAllInfoByAppId(applicationId);
        return ResponseSuccess.builder()
                .success(true)
                .time(LocalDateTime.now())
                .data(applicationDetail)
                .message("get application details successfully")
                .build();
    }

    @PatchMapping("/onboarding/application/{applicationId}/{status}")
    public GeneralResponse updateOnboardingApplication(@PathVariable int applicationId, @PathVariable String status, @RequestBody Comment comment) {
        applicationService.updateStatus(applicationId, status, comment);
        return ResponseSuccess.builder()
                .success(true)
                .time(LocalDateTime.now())
                .message("update application successfully")
                .build();
    }

    @GetMapping("/documents")
    public GeneralResponse getAllDocuments(){
        List<DigitalDocument> digitalDocuments = applicationService.getAllDocuments();
        return ResponseSuccess.builder()
                .success(true)
                .time(LocalDateTime.now())
                .data(digitalDocuments)
                .message("get all documents successfully")
                .build();
    }
}
