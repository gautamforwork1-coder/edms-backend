package com.edms.edms_backend.service.impl;

import com.edms.edms_backend.dto.request.EmployeeRequestDto;
import com.edms.edms_backend.dto.response.EmployeeResponseDto;
import com.edms.edms_backend.exception.ResourceAlreadyExistsException;
import com.edms.edms_backend.exception.ResourceNotFoundException;
import com.edms.edms_backend.model.Department;
import com.edms.edms_backend.model.Employee;
import com.edms.edms_backend.model.enums.EmployeeStatus;
import com.edms.edms_backend.repository.DepartmentRepository;
import com.edms.edms_backend.repository.EmployeeRepository;
import com.edms.edms_backend.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;

    @Override
    public EmployeeResponseDto createEmployee(EmployeeRequestDto requestDto) {
        log.info("Creating employee with email: {}", requestDto.getEmail());

        if (employeeRepository.existsByEmail(requestDto.getEmail())) {
            throw new ResourceAlreadyExistsException("Employee already exists with email: " + requestDto.getEmail());
        }

        Department department = departmentRepository.findById(requestDto.getDepartmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with id: " + requestDto.getDepartmentId()));

        Employee employee = Employee.builder()
                .firstName(requestDto.getFirstName())
                .lastName(requestDto.getLastName())
                .email(requestDto.getEmail())
                .phone(requestDto.getPhone())
                .salary(requestDto.getSalary())
                .jobTitle(requestDto.getJobTitle())
                .hireDate(requestDto.getHireDate())
                .gender(requestDto.getGender())
                .department(department)
                .status(EmployeeStatus.ACTIVE)
                .build();

        Employee savedEmployee = employeeRepository.save(employee);
        log.info("Employee created successfully with id: {}", savedEmployee.getId());

        return mapToResponseDto(savedEmployee);
    }

    @Override
    @Transactional(readOnly = true)
    public EmployeeResponseDto getEmployeeById(Long id) {
        log.info("Fetching employee with id: {}", id);
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + id));
        return mapToResponseDto(employee);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmployeeResponseDto> getAllEmployees() {
        log.info("Fetching all employees");
        return employeeRepository.findAll()
                .stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmployeeResponseDto> getEmployeesByDepartment(Long departmentId) {
        return employeeRepository.findByDepartmentId(departmentId)
                .stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmployeeResponseDto> getEmployeesByStatus(EmployeeStatus status) {
        return employeeRepository.findByStatus(status)
                .stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public EmployeeResponseDto updateEmployee(Long id, EmployeeRequestDto requestDto) {
        log.info("Updating employee with id: {}", id);

        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + id));

        Department department = departmentRepository.findById(requestDto.getDepartmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with id: " + requestDto.getDepartmentId()));

        employee.setFirstName(requestDto.getFirstName());
        employee.setLastName(requestDto.getLastName());
        employee.setPhone(requestDto.getPhone());
        employee.setSalary(requestDto.getSalary());
        employee.setJobTitle(requestDto.getJobTitle());
        employee.setHireDate(requestDto.getHireDate());
        employee.setGender(requestDto.getGender());
        employee.setDepartment(department);

        Employee updatedEmployee = employeeRepository.save(employee);
        log.info("Employee updated successfully with id: {}", updatedEmployee.getId());

        return mapToResponseDto(updatedEmployee);
    }

    @Override
    public EmployeeResponseDto updateEmployeeStatus(Long id, EmployeeStatus status) {
        log.info("Updating status of employee id: {} to {}", id, status);
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + id));
        employee.setStatus(status);
        return mapToResponseDto(employeeRepository.save(employee));
    }

    @Override
    public void deleteEmployee(Long id) {
        log.info("Deleting employee with id: {}", id);
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + id));
        employeeRepository.delete(employee);
        log.info("Employee deleted successfully with id: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmployeeResponseDto> searchEmployees(String name) {
        return employeeRepository.searchByName(name)
                .stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    private EmployeeResponseDto mapToResponseDto(Employee employee) {
        return EmployeeResponseDto.builder()
                .id(employee.getId())
                .firstName(employee.getFirstName())
                .lastName(employee.getLastName())
                .email(employee.getEmail())
                .phone(employee.getPhone())
                .salary(employee.getSalary())
                .jobTitle(employee.getJobTitle())
                .hireDate(employee.getHireDate())
                .status(employee.getStatus())
                .gender(employee.getGender())
                .departmentId(employee.getDepartment().getId())
                .departmentName(employee.getDepartment().getName())
                .createdAt(employee.getCreatedAt())
                .updatedAt(employee.getUpdatedAt())
                .build();
    }
}