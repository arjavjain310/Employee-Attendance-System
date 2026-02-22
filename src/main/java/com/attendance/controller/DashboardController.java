package com.attendance.controller;

import com.attendance.entity.InCharge;
import com.attendance.service.AttendanceService;
import com.attendance.service.EmployeeService;
import com.attendance.service.InChargeService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;
import java.util.Map;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {

    private final InChargeService inChargeService;
    private final EmployeeService employeeService;
    private final AttendanceService attendanceService;

    public DashboardController(InChargeService inChargeService, EmployeeService employeeService,
                               AttendanceService attendanceService) {
        this.inChargeService = inChargeService;
        this.employeeService = employeeService;
        this.attendanceService = attendanceService;
    }

    @GetMapping
    public String dashboard(@AuthenticationPrincipal User user, Model model) {
        InCharge inCharge = inChargeService.findByUserId(user.getUsername()).orElseThrow();
        LocalDate today = LocalDate.now();

        long totalEmployees = employeeService.countActive();
        long presentToday = attendanceService.countPresentToday(today);
        long absentToday = attendanceService.countAbsentToday(today);
        long notMarked = totalEmployees - presentToday - absentToday;

        Map<String, Object> monthlyTrends = attendanceService.getMonthlyTrends(6);
        ObjectMapper mapper = new ObjectMapper();
        try {
            model.addAttribute("monthlyLabelsJson", mapper.writeValueAsString(monthlyTrends.get("labels")));
            model.addAttribute("monthlyPresentJson", mapper.writeValueAsString(monthlyTrends.get("present")));
            model.addAttribute("monthlyAbsentJson", mapper.writeValueAsString(monthlyTrends.get("absent")));
        } catch (JsonProcessingException e) {
            model.addAttribute("monthlyLabelsJson", "[]");
            model.addAttribute("monthlyPresentJson", "[]");
            model.addAttribute("monthlyAbsentJson", "[]");
        }

        model.addAttribute("inCharge", inCharge);
        model.addAttribute("totalEmployees", totalEmployees);
        model.addAttribute("presentToday", presentToday);
        model.addAttribute("absentToday", absentToday);
        model.addAttribute("notMarked", notMarked);
        model.addAttribute("today", today);
        return "dashboard";
    }
}
