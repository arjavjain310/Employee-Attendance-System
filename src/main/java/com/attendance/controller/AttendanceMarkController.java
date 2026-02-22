package com.attendance.controller;

import com.attendance.entity.Employee;
import com.attendance.entity.InCharge;
import com.attendance.service.AttendanceService;
import com.attendance.service.EmployeeService;
import com.attendance.service.InChargeService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;

@Controller
@RequestMapping("/attendance")
public class AttendanceMarkController {

    private final InChargeService inChargeService;
    private final EmployeeService employeeService;
    private final AttendanceService attendanceService;

    public AttendanceMarkController(InChargeService inChargeService, EmployeeService employeeService,
                                    AttendanceService attendanceService) {
        this.inChargeService = inChargeService;
        this.employeeService = employeeService;
        this.attendanceService = attendanceService;
    }

    @PostMapping("/mark/{employeeId}")
    public String mark(@AuthenticationPrincipal User user, @PathVariable Long employeeId,
                       @RequestParam String status, RedirectAttributes redirectAttributes) {
        if (!"PRESENT".equals(status) && !"ABSENT".equals(status)) {
            redirectAttributes.addFlashAttribute("error", "Invalid status");
            return "redirect:/employees";
        }
        InCharge inCharge = inChargeService.findByUserId(user.getUsername()).orElseThrow();
        Employee employee = employeeService.findById(employeeId).orElse(null);
        if (employee == null) {
            redirectAttributes.addFlashAttribute("error", "Employee not found");
            return "redirect:/employees";
        }
        LocalDate today = LocalDate.now();
        attendanceService.updateOrMarkAttendance(employee, today, status, inCharge);
        redirectAttributes.addFlashAttribute("success", "Attendance marked for " + employee.getName());
        return "redirect:/employees";
    }
}
