package com.attendance.repository;

import com.attendance.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    Optional<Employee> findByEmployeeCode(String employeeCode);

    List<Employee> findAllByActiveTrueOrderByNameAsc();

    List<Employee> findAllByOrderByNameAsc();

    boolean existsByEmployeeCode(String employeeCode);

    long countByActiveTrue();
}
