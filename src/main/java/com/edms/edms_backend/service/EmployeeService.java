package com.edms.edms_backend.service;


import com.edms.edms_backend.dto.request.EmployeeRequestDto;
import com.edms.edms_backend.dto.response.EmployeeResponseDto;
import com.edms.edms_backend.model.enums.EmployeeStatus;

import java.util.List;

public interface EmployeeService {
    EmployeeResponseDto createEmployee(EmployeeRequestDto requestDto);

    EmployeeResponseDto getEmployeeById(Long id);

    List<EmployeeResponseDto> getAllEmployees();

    List<EmployeeResponseDto> getEmployeesByDepartment(Long departmentId);

    List<EmployeeResponseDto> getEmployeesByStatus(EmployeeStatus status);

    EmployeeResponseDto updateEmployee(Long id, EmployeeRequestDto requestDto);

    EmployeeResponseDto updateEmployeeStatus(Long id, EmployeeStatus status);

    void deleteEmployee(Long id);

    List<EmployeeResponseDto> searchEmployees(String name);
}
