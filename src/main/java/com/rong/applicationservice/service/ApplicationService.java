package com.rong.applicationservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.rong.applicationservice.dao.ApplicationWorkFlowDao;
import com.rong.applicationservice.dao.DigitalDocumentDao;
import com.rong.applicationservice.domain.*;
import com.rong.applicationservice.dto.request.Comment;
import com.rong.applicationservice.dto.request.OnboardingRequest;
import com.rong.applicationservice.dto.response.ApiResponse;
import com.rong.applicationservice.dto.response.ApplicationDetailResponse;
import com.rong.applicationservice.dto.response.DtoResponse;
import com.rong.applicationservice.exception.StatusDuplicateException;
import com.rong.applicationservice.service.remote.RemoteEmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ApplicationService {

    private final ApplicationWorkFlowDao applicationWorkFlowDao;
    private final DigitalDocumentDao digitalDocumentDao;
    private final RestTemplate restTemplate;
    private RemoteEmployeeService remoteEmployeeService;

    @Autowired
    public ApplicationService(RemoteEmployeeService remoteEmployeeService, ApplicationWorkFlowDao applicationWorkFlowDao, DigitalDocumentDao digitalDocumentDao, RestTemplate restTemplate) {
        this.remoteEmployeeService = remoteEmployeeService;
        this.applicationWorkFlowDao = applicationWorkFlowDao;
        this.digitalDocumentDao = digitalDocumentDao;
        this.restTemplate = restTemplate;
    }

    public int createOnboardingApplication(OnboardingRequest onboardingRequest) {
        List<Address> addressList = new ArrayList<>();
        addressList.add(onboardingRequest.getAddress());
        List<VisaStatus> visaStatusList = new ArrayList<>();
        visaStatusList.add(onboardingRequest.getVisaStatus());
        List<PersonalDocument> personalDocumentList = new ArrayList<>();
        personalDocumentList.add(PersonalDocument.builder()
                .path(onboardingRequest.getAvatar())
                .title("Avatar")
                .comment("Avatar")
                .createDate(LocalDate.now())
                .build());
        if(onboardingRequest.getWorkDoc() != null) {
            personalDocumentList.add(PersonalDocument.builder()
                    .path(onboardingRequest.getWorkDoc())
                    .title("Work Authorization")
                    .comment("Work Authorization")
                    .createDate(LocalDate.now())
                    .build());
        }
        personalDocumentList.add(PersonalDocument.builder()
                .path(onboardingRequest.getDriverLicense().getLicenseDoc())
                .title("Driver License")
                .comment("Driver License")
                .createDate(LocalDate.now())
                .build());
        ResponseEntity<DtoResponse> response = remoteEmployeeService.createEmployee(Employee.builder()
                .firstName(onboardingRequest.getFirstName())
                .lastName(onboardingRequest.getLastName())
                .preferredName(onboardingRequest.getPreferredName())
                .email(onboardingRequest.getEmail())
                .cellPhone(onboardingRequest.getCellPhone())
                .alternatePhone(onboardingRequest.getWorkPhone())
                .gender(onboardingRequest.getGender())
                .ssn(onboardingRequest.getSsn())
                .dob(onboardingRequest.getDob())
                .startDate(onboardingRequest.getStartDate())
                .endDate(onboardingRequest.getEndDate())
                .driverLicense(onboardingRequest.getDriverLicense().getLicenseNumber())
                .driverLicenseExpiration(onboardingRequest.getDriverLicense().getDriverLicenseExpiration())
                .contact(onboardingRequest.getContact())
                .address(addressList)
                .visaStatus(visaStatusList)
                .personalDocument(personalDocumentList)
                .build());
        DtoResponse dtoResponse = response.getBody();

        ApplicationWorkFlow applicationWorkFlow = ApplicationWorkFlow.builder()
                .employeeId((String) dtoResponse.getData())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .status("Pending")
                .comment("Awaiting HR review.")
                .applicationType("Onboarding")
                .build();
        int applicationId = applicationWorkFlowDao.add(applicationWorkFlow);
        personalDocumentList.forEach(
                personalDocument -> {
                    digitalDocumentDao.add(DigitalDocument.builder()
                            .employeeId((String) dtoResponse.getData())
                            .type(personalDocument.getTitle())
                            .title(personalDocument.getTitle())
                            .isRequired(true)
                            .path(personalDocument.getPath())
                            .description(personalDocument.getComment())
                            .build());
                });
        return applicationId;
    }

    public ResponseEntity<DtoResponse> getAllEmployee() {
        return remoteEmployeeService.getAllEmployees();
    }

    public ResponseEntity<DtoResponse> getEmployeeById(String userId) {
        return remoteEmployeeService.getEmployeeById(userId);
    }

    public Status getOnboardingStatusById(String userId) {
        ApplicationWorkFlow applicationWorkFlow = applicationWorkFlowDao.findByEmployeeIdAndApplicationType(userId, "Onboarding");
        return Status.builder().status(applicationWorkFlow.getStatus()).build();
    }

    public List<ApplicationWorkFlow> getAllOngoingApplications() {
        List<ApplicationWorkFlow> all =applicationWorkFlowDao.getAll();
        log.info(all.toString());
        return all.stream().filter(applicationWorkFlow -> !applicationWorkFlow.getStatus().equals("Completed")).collect(Collectors.toList());
    }

    public ApplicationDetailResponse getOngoingAllInfoByAppId(int applicationId) {
        ApplicationWorkFlow applicationWorkFlow = applicationWorkFlowDao.getOngoingByAppId(applicationId);
//        ResponseEntity<DtoResponse> response = restTemplate.exchange("http://employee-service/api/employees/{id}", HttpMethod.GET, null, DtoResponse.class, applicationWorkFlow.getEmployeeId());
        ResponseEntity<DtoResponse> dtoResponse = getEmployeeById(applicationWorkFlow.getEmployeeId());
        DtoResponse response = dtoResponse.getBody();
        Object body = response.getData();
        List<DigitalDocument> digitalDocuments = digitalDocumentDao.getAll();
        List<DigitalDocument> documents = digitalDocuments.stream().filter(digitalDocument -> digitalDocument.getEmployeeId().equals(applicationWorkFlow.getEmployeeId())).collect(Collectors.toList());
        return ApplicationDetailResponse.builder()
                .applicationWorkFlow(applicationWorkFlow)
                .employee(body)
                .digitalDocuments(documents)
                .build();
    }


    @Transactional
    public void updateStatus(int applicationId, String status, Comment comment) {
        log.info(applicationId + ":" + status + ":" + comment.getComment());
        ApplicationWorkFlow app = applicationWorkFlowDao.findById(applicationId);
        if (!app.getStatus().equals(status) && !app.getStatus().equals("Completed")) {
            applicationWorkFlowDao.updateStatus(applicationId, status, comment.getComment());
        } else{
            throw new StatusDuplicateException("Status already exists");
        }
    }

    public List<DigitalDocument> getAllDocuments() {
        return digitalDocumentDao.getAll();
    }
}
