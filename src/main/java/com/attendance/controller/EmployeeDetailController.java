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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/employee")
public class EmployeeDetailController {

    private final InChargeService inChargeService;
    private final EmployeeService employeeService;
    private final AttendanceService attendanceService;

    public EmployeeDetailController(InChargeService inChargeService, EmployeeService employeeService,
                                    AttendanceService attendanceService) {
        this.inChargeService = inChargeService;
        this.employeeService = employeeService;
        this.attendanceService = attendanceService;
    }

    @GetMapping("/{id}")
    public String detail(@AuthenticationPrincipal User user, @PathVariable Long id, Model model) {
        InCharge inCharge = inChargeService.findByUserId(user.getUsername()).orElseThrow();
        Employee employee = employeeService.findById(id).orElse(null);
        if (employee == null) {
            return "redirect:/employees";
        }
        List<Attendance> history = attendanceService.getAttendanceHistoryForEmployee(employee);

        model.addAttribute("inCharge", inCharge);
        model.addAttribute("employee", employee);
        model.addAttribute("attendanceHistory", history);
        return "employee-detail";
    }
}
