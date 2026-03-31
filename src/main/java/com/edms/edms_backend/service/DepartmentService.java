package com.edms.edms_backend.service;

import com.edms.edms_backend.dto.request.DepartmentRequestDto;
import com.edms.edms_backend.dto.response.DepartmentResponseDto;

import java.util.List;

public interface DepartmentService {
    DepartmentResponseDto createDepartment(DepartmentRequestDto requestDto);
    DepartmentResponseDto getDepartmentById(Long id);

    List<DepartmentResponseDto> getAllDepartments();

    List<DepartmentResponseDto> getActiveDepartments();

    DepartmentResponseDto updateDepartment(Long id, DepartmentRequestDto requestDto);

    void deleteDepartment(Long id);

    List<DepartmentResponseDto> searchDepartments(String name);
}
