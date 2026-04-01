package com.edms.edms_backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "departments")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name",nullable = false,length = 100)
    private String name;

    @Column(name = "code",unique = true,length = 20)
    private String code;

    @Column(name = "description",columnDefinition = "TEXT")
    private String description;

    @Column(name = "location",length = 100)
    private String location;


    @Column(name = "is_active")
    @Builder.Default
    private Boolean isActive = true;



    @OneToMany(mappedBy = "department",cascade = CascadeType.ALL, fetch=FetchType.LAZY)
    @Builder.Default
    private List<Employee> employees  = new ArrayList<>();

    @CreationTimestamp
    @Column(name = "createdAt",updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;


}
