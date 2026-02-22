package com.attendance.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Predefined employees (with Indian names) whose attendance is marked by in-charge users.
 * These are NOT login users.
 */
@Entity
@Table(name = "employees")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "employee_code", unique = true, nullable = false, length = 20)
    private String employeeCode;

    @Column(nullable = false, length = 100)
    @NotBlank(message = "Name is required")
    @Size(min = 1, max = 100)
    private String name;

    @Column(nullable = false)
    private boolean active = true;

    public Employee() {
    }

    public Employee(String employeeCode, String name) {
        this.employeeCode = employeeCode;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmployeeCode() {
        return employeeCode;
    }

    public void setEmployeeCode(String employeeCode) {
        this.employeeCode = employeeCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
