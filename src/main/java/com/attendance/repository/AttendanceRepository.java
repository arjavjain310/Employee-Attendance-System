package com.attendance.repository;

import com.attendance.entity.Attendance;
import com.attendance.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

    Optional<Attendance> findByEmployeeAndAttendanceDate(Employee employee, LocalDate date);

    boolean existsByEmployeeAndAttendanceDate(Employee employee, LocalDate date);

    List<Attendance> findByEmployeeOrderByAttendanceDateDesc(Employee employee, org.springframework.data.domain.Pageable pageable);

    List<Attendance> findByEmployeeOrderByAttendanceDateDesc(Employee employee);

    @Query("SELECT a FROM Attendance a WHERE a.attendanceDate = :date ORDER BY a.employee.name ASC")
    List<Attendance> findByAttendanceDate(@Param("date") LocalDate date);

    @Query("SELECT DISTINCT a FROM Attendance a JOIN FETCH a.employee LEFT JOIN FETCH a.markedBy WHERE a.attendanceDate BETWEEN :startDate AND :endDate ORDER BY a.attendanceDate DESC, a.markedAt ASC")
    List<Attendance> findByAttendanceDateBetweenWithMarkedBy(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("SELECT a FROM Attendance a LEFT JOIN FETCH a.markedBy WHERE a.employee = :employee ORDER BY a.attendanceDate DESC")
    List<Attendance> findByEmployeeOrderByAttendanceDateDescWithMarkedBy(@Param("employee") Employee employee);

    @Query("SELECT COUNT(a) FROM Attendance a WHERE a.attendanceDate = :date AND a.status = 'PRESENT'")
    long countPresentByDate(@Param("date") LocalDate date);

    @Query("SELECT COUNT(a) FROM Attendance a WHERE a.attendanceDate = :date AND a.status = 'ABSENT'")
    long countAbsentByDate(@Param("date") LocalDate date);

    @Query("SELECT a FROM Attendance a WHERE a.attendanceDate >= :since ORDER BY a.attendanceDate ASC")
    List<Attendance> findAllSince(@Param("since") LocalDate since);

    @Query("SELECT a FROM Attendance a WHERE a.attendanceDate BETWEEN :startDate AND :endDate ORDER BY a.attendanceDate DESC, a.markedAt ASC")
    List<Attendance> findByAttendanceDateBetween(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
}
