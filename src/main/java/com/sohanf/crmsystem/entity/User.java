package com.sohanf.crmsystem.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;


@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(schema = "public", name = "user")
public class User {

    @Id
    @GeneratedValue
    private Long id;
    private String firstName;
    private String lastName;
    private String subdomain;
    private String dob;
    private String phone;
    private String email;
    private String password;
    private boolean enabled;
    private String roles;

    public User(String firstName, String lastName, String subdomain, String dob, String phone, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.subdomain = subdomain;
        this.dob = dob;
        this.phone = phone;
        this.email = email;
    }
}
