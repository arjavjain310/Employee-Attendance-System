package com.attendance.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "attendance", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"employee_id", "attendance_date"})
})
public class Attendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @Column(name = "attendance_date", nullable = false)
    private LocalDate attendanceDate;

    @Column(name = "marked_at", nullable = false)
    private LocalDateTime markedAt;

    @Column(nullable = false, length = 20)
    private String status; // PRESENT or ABSENT

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "marked_by_id")
    private InCharge markedBy;

    public Attendance() {
    }

    public Attendance(Employee employee, LocalDate attendanceDate, LocalDateTime markedAt, String status) {
        this.employee = employee;
        this.attendanceDate = attendanceDate;
        this.markedAt = markedAt;
        this.status = status != null ? status : "PRESENT";
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public LocalDate getAttendanceDate() {
        return attendanceDate;
    }

    public void setAttendanceDate(LocalDate attendanceDate) {
        this.attendanceDate = attendanceDate;
    }

    public LocalDateTime getMarkedAt() {
        return markedAt;
    }

    public void setMarkedAt(LocalDateTime markedAt) {
        this.markedAt = markedAt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public InCharge getMarkedBy() {
        return markedBy;
    }

    public void setMarkedBy(InCharge markedBy) {
        this.markedBy = markedBy;
    }
}
