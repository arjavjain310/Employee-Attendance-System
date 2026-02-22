package com.attendance.config;

import com.attendance.entity.Employee;
import com.attendance.entity.InCharge;
import com.attendance.repository.EmployeeRepository;
import com.attendance.repository.InChargeRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@Configuration
public class DataInitializer {

    private static final List<String> INDIAN_EMPLOYEE_NAMES = List.of(
            "Rajesh Kumar", "Priya Sharma", "Amit Patel", "Sneha Reddy", "Vikram Singh",
            "Ananya Iyer", "Arjun Nair", "Kavitha Menon", "Suresh Pillai", "Lakshmi Venkat",
            "Rahul Desai", "Meera Joshi", "Karthik Rao", "Divya Krishnan", "Sanjay Gupta",
            "Pooja Saxena", "Manoj Tiwari", "Neha Agarwal", "Ravi Verma", "Shruti Malhotra",
            "Deepak Chopra", "Anjali Bhatia", "Nitin Kapoor", "Kiran Mehta", "Vivek Shah",
            "Rekha Pandey", "Sandeep Dubey", "Swati Trivedi", "Gaurav Mishra", "Preeti Sinha",
            "Aditya Banerjee", "Ritu Chatterjee", "Anil Das", "Pallavi Ghosh", "Rohan Bose"
    );

    @Bean
    public CommandLineRunner initData(EmployeeRepository employeeRepository,
                                     InChargeRepository inChargeRepository,
                                     PasswordEncoder passwordEncoder) {
        return args -> {
            if (inChargeRepository.count() == 0) {
                InCharge inCharge = new InCharge();
                inCharge.setUserId("12345");
                inCharge.setPassword(passwordEncoder.encode("password"));
                inCharge.setName("Default In-Charge");
                inCharge.setActive(true);
                inChargeRepository.save(inCharge);
            }

            if (employeeRepository.count() > 0) {
                return;
            }
            int i = 1;
            for (String name : INDIAN_EMPLOYEE_NAMES) {
                Employee emp = new Employee();
                emp.setEmployeeCode(String.format("EMP%03d", i++));
                emp.setName(name);
                emp.setActive(true);
                employeeRepository.save(emp);
            }
        };
    }
}
