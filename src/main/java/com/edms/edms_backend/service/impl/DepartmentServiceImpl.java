package com.edms.edms_backend.service.impl;

import com.edms.edms_backend.dto.request.DepartmentRequestDto;
import com.edms.edms_backend.dto.response.DepartmentResponseDto;
import com.edms.edms_backend.exception.ResourceAlreadyExistsException;
import com.edms.edms_backend.exception.ResourceNotFoundException;
import com.edms.edms_backend.model.Department;
import com.edms.edms_backend.repository.DepartmentRepository;
import com.edms.edms_backend.service.DepartmentService;
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
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;

    @Override
    public DepartmentResponseDto createDepartment(DepartmentRequestDto requestDto) {
        log.info("Creating department with name: {}", requestDto.getName());

        if (departmentRepository.existsByName(requestDto.getName())) {
            throw new ResourceAlreadyExistsException("Department already exists with name: " + requestDto.getName());
        }

        if (requestDto.getCode() != null && departmentRepository.existsByCode(requestDto.getCode())) {
            throw new ResourceAlreadyExistsException("Department already exists with code: " + requestDto.getCode());
        }

        Department department = Department.builder()
                .name(requestDto.getName())
                .code(requestDto.getCode())
                .description(requestDto.getDescription())
                .location(requestDto.getLocation())
                .isActive(true)
                .build();

        Department savedDepartment = departmentRepository.save(department);
        log.info("Department created successfully with id: {}", savedDepartment.getId());

        return mapToResponseDto(savedDepartment);
    }

    @Override
    @Transactional(readOnly = true)
    public DepartmentResponseDto getDepartmentById(Long id) {
        log.info("Fetching department with id: {}", id);
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with id: " + id));
        return mapToResponseDto(department);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DepartmentResponseDto> getAllDepartments() {
        log.info("Fetching all departments");
        return departmentRepository.findAll()
                .stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<DepartmentResponseDto> getActiveDepartments() {
        return departmentRepository.findByIsActiveTrue()
                .stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public DepartmentResponseDto updateDepartment(Long id, DepartmentRequestDto requestDto) {
        log.info("Updating department with id: {}", id);

        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with id: " + id));

        department.setName(requestDto.getName());
        department.setCode(requestDto.getCode());
        department.setDescription(requestDto.getDescription());
        department.setLocation(requestDto.getLocation());

        Department updatedDepartment = departmentRepository.save(department);
        log.info("Department updated successfully with id: {}", updatedDepartment.getId());

        return mapToResponseDto(updatedDepartment);
    }

    @Override
    public void deleteDepartment(Long id) {
        log.info("Deleting department with id: {}", id);
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with id: " + id));
        departmentRepository.delete(department);
        log.info("Department deleted successfully with id: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DepartmentResponseDto> searchDepartments(String name) {
        return departmentRepository.searchByName(name)
                .stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    // Private helper method — converts Entity to ResponseDto
    private DepartmentResponseDto mapToResponseDto(Department department) {
        return DepartmentResponseDto.builder()
                .id(department.getId())
                .name(department.getName())
                .code(department.getCode())
                .description(department.getDescription())
                .location(department.getLocation())
                .isActive(department.getIsActive())
                .totalEmployees(department.getEmployees().size())
                .createdAt(department.getCreatedAt())
                .updatedAt(department.getUpdatedAt())
                .build();
    }
}