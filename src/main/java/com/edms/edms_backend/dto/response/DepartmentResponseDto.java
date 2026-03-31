package com.edms.edms_backend.dto.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DepartmentResponseDto {

    private Long id;
    private String name;
    private String code;
    private String description;
    private String location;
    private boolean isActive;
    private int totalEmployees;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
