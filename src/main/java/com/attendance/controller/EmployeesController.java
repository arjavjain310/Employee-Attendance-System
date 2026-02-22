package com.attendance.controller;

import com.attendance.entity.Attendance;
import com.attendance.entity.Employee;
import com.attendance.entity.InCharge;
import com.attendance.service.AttendanceService;
import com.attendance.service.EmployeeService;
import com.attendance.service.InChargeService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/employees")
public class EmployeesController {

    private final InChargeService inChargeService;
    private final EmployeeService employeeService;
    private final AttendanceService attendanceService;

    public EmployeesController(InChargeService inChargeService, EmployeeService employeeService,
                               AttendanceService attendanceService) {
        this.inChargeService = inChargeService;
        this.employeeService = employeeService;
        this.attendanceService = attendanceService;
    }

    @GetMapping
    public String list(@AuthenticationPrincipal User user, Model model) {
        InCharge inCharge = inChargeService.findByUserId(user.getUsername()).orElseThrow();
        LocalDate today = LocalDate.now();
        List<Employee> employees = employeeService.findAllActive();
        List<Attendance> todayAttendance = attendanceService.getAttendanceByDate(today);
        Map<Long, String> statusByEmployeeId = todayAttendance.stream()
                .collect(Collectors.toMap(a -> a.getEmployee().getId(), Attendance::getStatus, (a, b) -> a));

        model.addAttribute("inCharge", inCharge);
        model.addAttribute("employees", employees);
        model.addAttribute("today", today);
        model.addAttribute("statusByEmployeeId", statusByEmployeeId);
        return "employees";
    }
}
