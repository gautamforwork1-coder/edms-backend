package com.edms.edms_backend.controller;

import com.edms.edms_backend.dto.request.EmployeeRequestDto;
import com.edms.edms_backend.dto.response.EmployeeResponseDto;
import com.edms.edms_backend.model.enums.EmployeeStatus;
import com.edms.edms_backend.service.EmployeeService;
import com.edms.edms_backend.util.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/employees")
@RequiredArgsConstructor
@Slf4j
public class EmployeeController {

    private final EmployeeService employeeService;

    @PostMapping
    public ResponseEntity<ApiResponse<EmployeeResponseDto>> createEmployee(
            @Valid @RequestBody EmployeeRequestDto requestDto) {
        log.info("REST request to create employee: {}", requestDto.getEmail());
        EmployeeResponseDto response = employeeService.createEmployee(requestDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Employee created successfully", response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<EmployeeResponseDto>>> getAllEmployees() {
        log.info("REST request to get all employees");
        List<EmployeeResponseDto> employees = employeeService.getAllEmployees();
        return ResponseEntity.ok(ApiResponse.success("Employees retrieved successfully", employees));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<EmployeeResponseDto>> getEmployeeById(@PathVariable Long id) {
        log.info("REST request to get employee by id: {}", id);
        EmployeeResponseDto employee = employeeService.getEmployeeById(id);
        return ResponseEntity.ok(ApiResponse.success("Employee retrieved successfully", employee));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<EmployeeResponseDto>> updateEmployee(
            @PathVariable Long id,
            @Valid @RequestBody EmployeeRequestDto requestDto) {
        log.info("REST request to update employee id: {}", id);
        EmployeeResponseDto response = employeeService.updateEmployee(id, requestDto);
        return ResponseEntity.ok(ApiResponse.success("Employee updated successfully", response));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteEmployee(@PathVariable Long id) {
        log.info("REST request to delete employee id: {}", id);
        employeeService.deleteEmployee(id);
        return ResponseEntity.ok(ApiResponse.success("Employee deleted successfully"));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse<EmployeeResponseDto>> updateEmployeeStatus(
            @PathVariable Long id,
            @RequestParam EmployeeStatus status) {
        log.info("REST request to update status of employee id: {} to {}", id, status);
        EmployeeResponseDto response = employeeService.updateEmployeeStatus(id, status);
        return ResponseEntity.ok(ApiResponse.success("Employee status updated successfully", response));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<EmployeeResponseDto>>> searchEmployees(
            @RequestParam String name) {
        log.info("REST request to search employees by name: {}", name);
        List<EmployeeResponseDto> employees = employeeService.searchEmployees(name);
        return ResponseEntity.ok(ApiResponse.success("Search completed successfully", employees));
    }

    @GetMapping("/department/{departmentId}")
    public ResponseEntity<ApiResponse<List<EmployeeResponseDto>>> getEmployeesByDepartment(
            @PathVariable Long departmentId) {
        log.info("REST request to get employees by department id: {}", departmentId);
        List<EmployeeResponseDto> employees = employeeService.getEmployeesByDepartment(departmentId);
        return ResponseEntity.ok(ApiResponse.success("Employees retrieved successfully", employees));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<ApiResponse<List<EmployeeResponseDto>>> getEmployeesByStatus(
            @PathVariable EmployeeStatus status) {
        log.info("REST request to get employees by status: {}", status);
        List<EmployeeResponseDto> employees = employeeService.getEmployeesByStatus(status);
        return ResponseEntity.ok(ApiResponse.success("Employees retrieved successfully", employees));
    }
}