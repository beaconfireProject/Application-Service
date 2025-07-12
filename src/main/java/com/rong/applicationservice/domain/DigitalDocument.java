package com.rong.applicationservice.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "DigitalDocument")
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DigitalDocument {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;

    @Column(name = "EmployeeID")
    private Integer employeeId;

    @Column(name = "Type")
    private String type;

    @Column(name = "isRequired")
    private boolean isRequired;

    @Column(name = "Path")
    private String path;

    @Column(name = "Description")
    private String description;

    @Column(name = "Title")
    private String title;
}
