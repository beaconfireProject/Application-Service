package com.rong.applicationservice.domain;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "ApplicationWorkFlow")
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationWorkFlow {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;

    @Column(name = "EmployeeID")
    private String employeeId;

    @Column(name = "CreateDate", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "LastModificationDate", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "Status", nullable = false)
    private String status;

    @Column(name = "Comment", nullable = false)
    private String comment;

    @Column(name = "ApplicationType")
    private String applicationType;
}
