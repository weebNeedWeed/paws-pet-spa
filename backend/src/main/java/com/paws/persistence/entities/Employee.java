package com.paws.persistence.entities;

import com.paws.persistence.common.enums.Gender;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "employees")
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false)
    private LocalDate dateOfBirth;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String phoneNumber;

    @Column(nullable = false)
    private Gender gender;

    @Column(nullable = false)
    private LocalDate firstWorkingDay;

    private LocalDate quitDate;

    @Column(unique = true)
    private String identityNumber;

    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE})
    @JoinTable(name = "employee_appointment_item",
            joinColumns = {@JoinColumn(name = "employee_id")},
            inverseJoinColumns = {@JoinColumn(name = "appointment_item_id")})
    private Set<AppointmentItem> appointmentItems = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "employees")
    private Set<Role> roles = new HashSet<>();

    public Employee() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public LocalDate getFirstWorkingDay() {
        return firstWorkingDay;
    }

    public void setFirstWorkingDay(LocalDate firstWorkingDay) {
        this.firstWorkingDay = firstWorkingDay;
    }

    public LocalDate getQuitDate() {
        return quitDate;
    }

    public void setQuitDate(LocalDate quitDate) {
        this.quitDate = quitDate;
    }

    public String getIdentityNumber() {
        return identityNumber;
    }

    public void setIdentityNumber(String identityNumber) {
        this.identityNumber = identityNumber;
    }

    public Set<Role> getRoles() {
        return roles;
    }
}
