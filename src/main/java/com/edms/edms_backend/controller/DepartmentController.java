package com.edms.edms_backend.controller;

import com.edms.edms_backend.dto.request.DepartmentRequestDto;
import com.edms.edms_backend.dto.response.DepartmentResponseDto;
import com.edms.edms_backend.service.DepartmentService;
import com.edms.edms_backend.util.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/departments")
@RequiredArgsConstructor
@Slf4j
public class DepartmentController {

    private final DepartmentService departmentService;

    @PostMapping
    public ResponseEntity<ApiResponse<DepartmentResponseDto>> createDepartment(
            @Valid @RequestBody DepartmentRequestDto requestDto) {
        log.info("REST request to create department: {}", requestDto.getName());
        DepartmentResponseDto response = departmentService.createDepartment(requestDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Department created successfully", response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<DepartmentResponseDto>>> getAllDepartments() {
        log.info("REST request to get all departments");
        List<DepartmentResponseDto> departments = departmentService.getAllDepartments();
        return ResponseEntity.ok(ApiResponse.success("Departments retrieved successfully", departments));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<DepartmentResponseDto>> getDepartmentById(@PathVariable Long id) {
        log.info("REST request to get department by id: {}", id);
        DepartmentResponseDto department = departmentService.getDepartmentById(id);
        return ResponseEntity.ok(ApiResponse.success("Department retrieved successfully", department));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<DepartmentResponseDto>> updateDepartment(
            @PathVariable Long id,
            @Valid @RequestBody DepartmentRequestDto requestDto) {
        log.info("REST request to update department id: {}", id);
        DepartmentResponseDto response = departmentService.updateDepartment(id, requestDto);
        return ResponseEntity.ok(ApiResponse.success("Department updated successfully", response));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteDepartment(@PathVariable Long id) {
        log.info("REST request to delete department id: {}", id);
        departmentService.deleteDepartment(id);
        return ResponseEntity.ok(ApiResponse.success("Department deleted successfully"));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<DepartmentResponseDto>>> searchDepartments(
            @RequestParam String name) {
        log.info("REST request to search departments by name: {}", name);
        List<DepartmentResponseDto> departments = departmentService.searchDepartments(name);
        return ResponseEntity.ok(ApiResponse.success("Search completed successfully", departments));
    }

    @GetMapping("/active")
    public ResponseEntity<ApiResponse<List<DepartmentResponseDto>>> getActiveDepartments() {
        log.info("REST request to get active departments");
        List<DepartmentResponseDto> departments = departmentService.getActiveDepartments();
        return ResponseEntity.ok(ApiResponse.success("Active departments retrieved successfully", departments));
    }
}