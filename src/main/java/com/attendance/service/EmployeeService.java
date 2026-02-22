package com.attendance.service;

import com.attendance.entity.Employee;
import com.attendance.repository.EmployeeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Transactional(readOnly = true)
    public List<Employee> findAllActive() {
        return employeeRepository.findAllByActiveTrueOrderByNameAsc();
    }

    @Transactional(readOnly = true)
    public List<Employee> findAll() {
        return employeeRepository.findAllByOrderByNameAsc();
    }

    @Transactional(readOnly = true)
    public Optional<Employee> findById(Long id) {
        return employeeRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public long countActive() {
        return employeeRepository.countByActiveTrue();
    }

    @Transactional
    public Employee addEmployee(String employeeCode, String name) {
        Employee emp = new Employee();
        emp.setEmployeeCode(employeeCode != null ? employeeCode.trim() : "");
        emp.setName(name != null ? name.trim() : "");
        emp.setActive(true);
        return employeeRepository.save(emp);
    }

    @Transactional
    public void removeEmployee(Long id) {
        employeeRepository.findById(id).ifPresent(emp -> {
            emp.setActive(false);
            employeeRepository.save(emp);
        });
    }

    public boolean existsByEmployeeCode(String employeeCode) {
        return employeeRepository.existsByEmployeeCode(employeeCode);
    }
}
