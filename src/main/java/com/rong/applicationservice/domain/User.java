package com.rong.applicationservice.domain;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "User")
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;

    @Column(name = "Username", nullable = false, unique = true)
    private String username;

    @Column(name = "Password", nullable = false)
    private String password;

    @Column(name = "Email", nullable = false, unique = true)
    private String email;

    @Column(name = "CreateDate", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "LastModificationDate", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "ActiveFlag")
    private int isActive;
}
