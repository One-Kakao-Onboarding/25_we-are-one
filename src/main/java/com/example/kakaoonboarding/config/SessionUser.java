package com.example.kakaoonboarding.config;

import com.example.kakaoonboarding.entity.UserRole;

import java.io.Serializable;

/**
 * 세션에 저장될 사용자 정보
 */
public class SessionUser implements Serializable {
    private Long id;
    private String username;
    private String name;
    private UserRole role;
    private String employeeId;
    private String department;
    private String companyId;

    public SessionUser(Long id, String username, String name, UserRole role,
                      String employeeId, String department, String companyId) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.role = role;
        this.employeeId = employeeId;
        this.department = department;
        this.companyId = companyId;
    }

    // Getters and Setters
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public boolean isEmployee() {
        return role == UserRole.EMPLOYEE;
    }

    public boolean isConsultant() {
        return role == UserRole.CONSULTANT;
    }
}
