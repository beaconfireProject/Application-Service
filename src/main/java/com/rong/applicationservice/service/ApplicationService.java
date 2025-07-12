package com.rong.applicationservice.service;

import com.rong.applicationservice.dao.ApplicationWorkFlowDao;
import com.rong.applicationservice.dao.DigitalDocumentDao;
import com.rong.applicationservice.domain.ApplicationWorkFlow;
import com.rong.applicationservice.domain.DigitalDocument;
import com.rong.applicationservice.domain.Employee;
import com.rong.applicationservice.dto.response.ApiResponse;
import com.rong.applicationservice.service.remote.RemoteEmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
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

    public int createOnboardingApplication(Employee employee) {
        ApiResponse apiResponse = (ApiResponse) remoteEmployeeService.createEmployee(employee).getBody();
        int id = Integer.parseInt(apiResponse.getId().substring(1));
        ApplicationWorkFlow applicationWorkFlow = ApplicationWorkFlow.builder()
                .employeeId(id)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .status("Pending")
                .comment("Awaiting HR review.")
                .applicationType("Onboarding")
                .build();
        int applicationId = applicationWorkFlowDao.add(applicationWorkFlow);
        employee.getPersonalDocument().forEach(
                personalDocument -> {
                    digitalDocumentDao.add(DigitalDocument.builder()
                            .employeeId(id)
                            .type(personalDocument.getTitle())
                            .title(personalDocument.getTitle())
                            .isRequired(true)
                            .path(personalDocument.getPath())
                            .description(personalDocument.getComment())
                            .build());
                });
        return applicationId;
    }

    public ResponseEntity<List<Employee>> getAllEmployee() {
        return remoteEmployeeService.getAllEmployees();
    }
}
