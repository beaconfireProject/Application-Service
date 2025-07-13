package com.rong.applicationservice.service.remote;

import com.rong.applicationservice.domain.Employee;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@FeignClient(name = "employee-service")
public interface RemoteEmployeeService {

    @PostMapping("/employee-service/api/employee")
    ResponseEntity<?> createEmployee(Employee employee);

    @GetMapping("/employee-service/api/employee")
    ResponseEntity<List<Employee>> getAllEmployees();

    @GetMapping("/employee-service/api/employee/{id}")
    ResponseEntity<Employee> getEmployeeById(@PathVariable String id);
}
