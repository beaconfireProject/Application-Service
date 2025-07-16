package com.rong.applicationservice.service;

import com.rong.applicationservice.dao.ApplicationWorkFlowDao;
import com.rong.applicationservice.dao.DigitalDocumentDao;
import com.rong.applicationservice.domain.*;
import com.rong.applicationservice.dto.request.*;
import com.rong.applicationservice.dto.response.ApplicationDetailResponse;
import com.rong.applicationservice.dto.response.DtoResponse;
import com.rong.applicationservice.exception.EmployeeException;
import com.rong.applicationservice.exception.StatusException;
import com.rong.applicationservice.service.remote.RemoteEmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ApplicationService {

    private final ApplicationWorkFlowDao applicationWorkFlowDao;
    private final DigitalDocumentDao digitalDocumentDao;
    private RemoteEmployeeService remoteEmployeeService;

    @Autowired
    public ApplicationService(RemoteEmployeeService remoteEmployeeService, ApplicationWorkFlowDao applicationWorkFlowDao, DigitalDocumentDao digitalDocumentDao) {
        this.remoteEmployeeService = remoteEmployeeService;
        this.applicationWorkFlowDao = applicationWorkFlowDao;
        this.digitalDocumentDao = digitalDocumentDao;
    }

    @Transactional
    public int createOnboardingApplication(OnboardingRequest onboardingRequest) {
        if (checkStatus(onboardingRequest.getStatus()) && checkType(onboardingRequest.getApplicationType())) {
            ApplicationWorkFlow applicationWorkFlow = ApplicationWorkFlow.builder()
                    .employeeId(onboardingRequest.getEmployeeId())
                    .createdAt(onboardingRequest.getCreateDate())
                    .updatedAt(onboardingRequest.getLastModificationDate())
                    .status(onboardingRequest.getStatus())
                    .comment(onboardingRequest.getComment())
                    .applicationType(onboardingRequest.getApplicationType()).build();
            int id = applicationWorkFlowDao.add(applicationWorkFlow);
            return id;
        } else{
            throw new StatusException("Application type or status does not exist");
        }
//        List<Address> addressList = new ArrayList<>();
//        addressList.add(onboardingRequest.getAddress());
//        List<VisaStatus> visaStatusList = new ArrayList<>();
//        visaStatusList.add(onboardingRequest.getVisaStatus());
//        List<PersonalDocument> personalDocumentList = new ArrayList<>();
//        personalDocumentList.add(PersonalDocument.builder()
//                .path(onboardingRequest.getAvatar())
//                .title("Avatar")
//                .comment("Avatar")
//                .createDate(LocalDate.now())
//                .build());
//        if(onboardingRequest.getWorkDoc() != null) {
//            personalDocumentList.add(PersonalDocument.builder()
//                    .path(onboardingRequest.getWorkDoc())
//                    .title("Work Authorization")
//                    .comment("Work Authorization")
//                    .createDate(LocalDate.now())
//                    .build());
//        }
//        personalDocumentList.add(PersonalDocument.builder()
//                .path(onboardingRequest.getDriverLicense().getLicenseDoc())
//                .title("Driver License")
//                .comment("Driver License")
//                .createDate(LocalDate.now())
//                .build());
//        try {
//            ResponseEntity<DtoResponse<String>> response = remoteEmployeeService.createEmployee(Employee.builder()
//                    .userId(userId.toString())
//                    .firstName(onboardingRequest.getFirstName())
//                    .lastName(onboardingRequest.getLastName())
//                    .preferredName(onboardingRequest.getPreferredName())
//                    .email(onboardingRequest.getEmail())
//                    .cellPhone(onboardingRequest.getCellPhone())
//                    .alternatePhone(onboardingRequest.getWorkPhone())
//                    .gender(onboardingRequest.getGender())
//                    .ssn(onboardingRequest.getSsn())
//                    .dob(onboardingRequest.getDob())
//                    .startDate(onboardingRequest.getStartDate())
//                    .endDate(onboardingRequest.getEndDate())
//                    .driverLicense(onboardingRequest.getDriverLicense().getLicenseNumber())
//                    .driverLicenseExpiration(onboardingRequest.getDriverLicense().getDriverLicenseExpiration())
//                    .contact(onboardingRequest.getContact())
//                    .address(addressList)
//                    .visaStatus(visaStatusList)
//                    .personalDocument(personalDocumentList)
//                    .build());
//            ApplicationWorkFlow applicationWorkFlow = ApplicationWorkFlow.builder()
//                    .employeeId(response.getBody().getData())
//                    .createdAt(LocalDateTime.now())
//                    .updatedAt(LocalDateTime.now())
//                    .status("Pending")
//                    .comment("Awaiting HR review.")
//                    .applicationType("Onboarding")
//                    .build();
//            int applicationId = applicationWorkFlowDao.add(applicationWorkFlow);
//            personalDocumentList.forEach(
//                    personalDocument -> {
//                        digitalDocumentDao.add(DigitalDocument.builder()
//                                .employeeId(response.getBody().getData())
//                                .type(personalDocument.getTitle())
//                                .title(personalDocument.getTitle())
//                                .isRequired(true)
//                                .path(personalDocument.getPath())
//                                .description(personalDocument.getComment())
//                                .build());
//                    });
//            return applicationId;
//        } catch (Exception e){
//            throw new EmployeeException("Failed to create onboarding application");
//        }
    }

    private boolean checkType(String type) {
        List<String> appTypes = new ArrayList<>();
        appTypes.add("Onboarding");
        appTypes.add("Visa_I983");
        appTypes.add("Visa_OPT");
        appTypes.add("Visa_STEM_Receipt");
        appTypes.add("Visa_OPT_STEM_EAD");
        if (!appTypes.contains(type)) {
            return false;
        }
        return true;
    }

    public ResponseEntity<DtoResponse<List<Employee>>> getAllEmployee() {
        return remoteEmployeeService.getAllEmployees();
    }

    public ResponseEntity<DtoResponse<Employee>> getEmployeeById(String userId) {
        try {
            return remoteEmployeeService.getEmployeeById(userId);
        } catch (Exception e){
            throw new EmployeeException("Failed to get employee by id");
        }
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
        ResponseEntity<DtoResponse<Employee>> dtoResponse = getEmployeeById(applicationWorkFlow.getEmployeeId());
        DtoResponse<Employee> response = dtoResponse.getBody();
        Employee employee = response.getData();
        List<DigitalDocument> digitalDocuments = digitalDocumentDao.getAll();
        List<DigitalDocument> documents = digitalDocuments.stream().filter(digitalDocument -> digitalDocument.getEmployeeId().equals(applicationWorkFlow.getEmployeeId())).collect(Collectors.toList());
        return ApplicationDetailResponse.builder()
                .applicationWorkFlow(applicationWorkFlow)
                .employee(employee)
                .digitalDocuments(documents)
                .build();
    }


    @Transactional
    public void updateStatus(int applicationId, StatusRequest statusRequest) {
        String status = statusRequest.getStatus();
        if(checkStatus(status)) {
            ApplicationWorkFlow app = applicationWorkFlowDao.findById(applicationId);
            if (!app.getStatus().equals(status) && !app.getStatus().equals("Completed")) {
                app.setStatus(status);
                applicationWorkFlowDao.update(app);
            } else {
                throw new StatusException("Status already exists or completed");
            }
        } else{
            throw new StatusException("Status is not supported");
        }
    }

    public List<DigitalDocument> getAllDocuments() {
        return digitalDocumentDao.getAll();
    }

    @Transactional
    public void updateTime(int applicationId, TimeRequest timeRequest) {
        ApplicationWorkFlow app = applicationWorkFlowDao.findById(applicationId);
        app.setUpdatedAt(timeRequest.getLastModificationDate());
        applicationWorkFlowDao.update(app);
    }

    private boolean checkStatus(String status) {
        List<String> statusList = new ArrayList<>();
        statusList.add("Completed");
        statusList.add("Pending");
        statusList.add("Approved");
        statusList.add("Rejected");
        return statusList.contains(status);
    }

    @Transactional
    public void updateComment(int applicationId, CommentRequest commentRequest) {
        ApplicationWorkFlow app = applicationWorkFlowDao.findById(applicationId);
        app.setComment(commentRequest.getComment());
        applicationWorkFlowDao.update(app);
    }
}
