package com.bmw.store.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;

    private String lastName;

    @Column(unique = true)
    private String email;

    private String password;

    private LocalDateTime createdDate;

    private String status;

    private String role;

    private LocalDateTime lastLogin;

    private String profilePicture;

    private String phoneNumber;

    private String address;


}