package com.attendance.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class AddEmployeeDto {

    @NotBlank(message = "Employee code is required")
    @Size(min = 1, max = 20)
    private String employeeCode;

    @NotBlank(message = "Name is required")
    @Size(min = 1, max = 100)
    private String name;

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
}
