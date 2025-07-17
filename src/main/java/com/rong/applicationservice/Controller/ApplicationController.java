package com.rong.applicationservice.Controller;

import com.rong.applicationservice.domain.ApplicationWorkFlow;
import com.rong.applicationservice.domain.DigitalDocument;
import com.rong.applicationservice.domain.Employee;
import com.rong.applicationservice.domain.Status;
import com.rong.applicationservice.dto.request.*;
import com.rong.applicationservice.dto.response.*;
import com.rong.applicationservice.exception.ApplicationNotFoundException;
import com.rong.applicationservice.exception.AuthorizationNotFoundException;
import com.rong.applicationservice.service.ApplicationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/application")
@Slf4j
public class ApplicationController {

    private final ApplicationService applicationService;

    public ApplicationController(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    private boolean checkUserID(String employeeUserId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated()) {
            for(GrantedAuthority role : auth.getAuthorities())
            {
                if(role.getAuthority().equals("HR")) {
                    return true;
                }
            }
            Object userIdObj = auth.getDetails();
            if (userIdObj instanceof Long) {
                Long userId = (Long) userIdObj;
                return userId.toString().equals(employeeUserId);
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    @PostMapping("/onboarding")
    @PreAuthorize("hasAuthority('EMPLOYEE')")
    public GeneralResponse createOnboardingApplication(@RequestBody OnboardingRequest onboardingRequest) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated()) {
            Object userIdObj = auth.getDetails();
            if (userIdObj instanceof Long) {
                Long userId = (Long) userIdObj;
                log.info("User ID from JWT token: {}", userId);
                int id = applicationService.createOnboardingApplication(onboardingRequest);
                return ResponseSuccess.builder()
                        .success(true)
                        .time(LocalDateTime.now())
                        .data(ApplicationDataResponse.builder().id(id).build())
                        .message("Create application successfully").build();
            } else {
                throw new AuthorizationNotFoundException("No User ID from JWT token");
            }
        } else {
            throw new AuthorizationNotFoundException("No user found in the authentication");
        }
    }

    @GetMapping("/employee")
    @PreAuthorize("hasAuthority('HR')")
    public ResponseEntity<DtoResponse<List<Employee>>> getAllEmployee() {
        return applicationService.getAllEmployee();
    }

    @GetMapping("/onboarding/{userId}")
    public ResponseEntity<DtoResponse<Employee>> getEmployeeById(@PathVariable String userId) {
        ResponseEntity<DtoResponse<Employee>> res = applicationService.getEmployeeById(userId);
        Employee e = res.getBody().getData();
        if(checkUserID(e.getUserId())) {
            return res;
        } else {
            throw new AuthorizationNotFoundException("User is not authorized");
        }
    }

    @GetMapping("/onboarding/status/{userId}")
    public GeneralResponse checkStatus(@PathVariable String userId) {
        ResponseEntity<DtoResponse<Employee>> res = applicationService.getEmployeeById(userId);
        Employee e = res.getBody().getData();
        if(checkUserID(e.getUserId())) {
            Status status = applicationService.getOnboardingStatusById(userId);
            if (status != null) {
                return ResponseSuccess.builder()
                        .success(true)
                        .time(LocalDateTime.now())
                        .data(status)
                        .message("get status by id successfully").build();
            } else {
                throw new ApplicationNotFoundException("Application does not exist");
            }
        } else {
            throw new AuthorizationNotFoundException("User is not authorized");
        }
    }

    @GetMapping("/onboarding/applications")
    @PreAuthorize("hasAuthority('HR')")
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
        ApplicationDetailResponse appInfo = applicationService.getOngoingAllInfoByAppId(applicationId);
        if (appInfo == null) {
            throw new ApplicationNotFoundException("Application does not exist");
        }
        Employee e = appInfo.getEmployee();
        if(checkUserID(e.getUserId())) {
            return ResponseSuccess.builder()
                    .success(true)
                    .time(LocalDateTime.now())
                    .data(appInfo)
                    .message("get application details successfully")
                    .build();
        } else{
            throw new AuthorizationNotFoundException("User is not authorized");
        }
    }

//    @PatchMapping("/onboarding/application/{applicationId}/{status}")
//    @PreAuthorize("hasAuthority('HR')")
//    public GeneralResponse updateOnboardingApplication(@PathVariable int applicationId, @PathVariable String status, @RequestBody Comment comment) {
//        applicationService.updateStatus(applicationId, status, comment);
//        return ResponseSuccess.builder()
//                .success(true)
//                .time(LocalDateTime.now())
//                .message("update application successfully")
//                .build();
//    }

    @PatchMapping("/{applicationId}/time")
    @PreAuthorize("hasAuthority('HR')")
    public GeneralResponse updateTime(@PathVariable int applicationId, @RequestBody TimeRequest timeRequest){
        applicationService.updateTime(applicationId, timeRequest);
        return ResponseSuccess.builder()
                .success(true)
                .time(LocalDateTime.now())
                .message("update application successfully")
                .build();
    }

    @PatchMapping("/{applicationId}/status")
    @PreAuthorize("hasAuthority('HR')")
    public GeneralResponse updateStatus(@PathVariable int applicationId, @RequestBody StatusRequest statusRequest){
        applicationService.updateStatus(applicationId, statusRequest);
        return ResponseSuccess.builder()
                .success(true)
                .time(LocalDateTime.now())
                .message("update application successfully")
                .build();
    }

    @PatchMapping("/{applicationId}/comment")
    @PreAuthorize("hasAuthority('HR')")
    public GeneralResponse updateComment(@PathVariable int applicationId, @RequestBody CommentRequest commentRequest){
        applicationService.updateComment(applicationId, commentRequest);
        return ResponseSuccess.builder()
                .success(true)
                .time(LocalDateTime.now())
                .message("update application successfully")
                .build();
    }

    @GetMapping("/documents")
    @PreAuthorize("hasAuthority('HR')")
    public GeneralResponse getAllDocuments(){
        List<DigitalDocument> digitalDocuments = applicationService.getAllDocuments();
        return ResponseSuccess.builder()
                .success(true)
                .time(LocalDateTime.now())
                .data(digitalDocuments)
                .message("get all documents successfully")
                .build();
    }

    @GetMapping("/check/{empId}")
    @PreAuthorize("hasAuthority('HR')")
    public GeneralResponse checkApplication(@PathVariable String empId) {
        ApplicationWorkFlow existApplication = applicationService.checkApplication(empId);
        String message;
        if(existApplication == null) {
            message = "Ongoing Application does not exist";
        } else {
            message = "Get Ongoing Application successfully";
        }
        return ResponseSuccess.builder()
                .success(true)
                .time(LocalDateTime.now())
                .data(existApplication)
                .message(message)
                .build();
    }

    @GetMapping("/check")
    @PreAuthorize("hasAuthority('EMPLOYEE')")
    public GeneralResponse checkApplicationByEmp() {
        ApplicationWorkFlow applicationWorkFlow = applicationService.checkApplicationByEmp();
        String message;
        if(applicationWorkFlow == null) {
            message = "Ongoing Application does not exist";
        } else {
            message = "Get Ongoing Application successfully";
        }
        return ResponseSuccess.builder()
                .success(true)
                .time(LocalDateTime.now())
                .data(applicationWorkFlow)
                .message(message)
                .build();
    }
}
