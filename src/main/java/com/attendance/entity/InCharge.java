package com.attendance.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "in_charge")
public class InCharge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", unique = true, nullable = false, length = 5)
    @NotBlank(message = "User ID is required")
    @Pattern(regexp = "^[0-9]{5}$", message = "User ID must be exactly 5 digits")
    private String userId;

    @Column(nullable = false, length = 255)
    private String password;

    @Column(nullable = false, length = 100)
    @NotBlank(message = "Name is required")
    @Size(min = 1, max = 100)
    private String name;

    @Column(nullable = false)
    private boolean active = true;

    public InCharge() {
    }

    public InCharge(String userId, String password, String name) {
        this.userId = userId;
        this.password = password;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
