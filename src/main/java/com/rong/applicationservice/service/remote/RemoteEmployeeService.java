package com.rong.applicationservice.service.remote;

import com.rong.applicationservice.domain.Employee;
import com.rong.applicationservice.dto.response.ApiResponse;
import com.rong.applicationservice.dto.response.DtoResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@FeignClient(name = "employee-service")
public interface RemoteEmployeeService {

    @PostMapping("/api/employees")
    ResponseEntity<DtoResponse> createEmployee(Employee employee);

    @GetMapping("/api/employees")
    ResponseEntity<DtoResponse> getAllEmployees();

    @GetMapping("/api/employees/{id}")
    ResponseEntity<DtoResponse> getEmployeeById(@PathVariable String id);
}
