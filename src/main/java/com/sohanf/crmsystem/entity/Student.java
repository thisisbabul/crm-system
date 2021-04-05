package com.sohanf.crmsystem.entity;

import com.sohanf.crmsystem.multitenant.TenantContext;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "student")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String parentName;
    private String parentContact;
    private String password;
    private String roles;
}
