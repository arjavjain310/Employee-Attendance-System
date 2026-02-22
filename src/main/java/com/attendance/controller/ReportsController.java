package com.attendance.controller;

import com.attendance.entity.Attendance;
import com.attendance.entity.InCharge;
import com.attendance.service.AttendanceService;
import com.attendance.service.InChargeService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;

/**
 * Reports show previous attendance records (visible to all logged-in users).
 * Records retained for at least one month.
 */
@Controller
@RequestMapping("/reports")
public class ReportsController {

    private final InChargeService inChargeService;
    private final AttendanceService attendanceService;

    public ReportsController(InChargeService inChargeService, AttendanceService attendanceService) {
        this.inChargeService = inChargeService;
        this.attendanceService = attendanceService;
    }

    @GetMapping
    public String reports(
            @AuthenticationPrincipal User user,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
            Model model) {
        InCharge inCharge = inChargeService.findByUserId(user.getUsername()).orElseThrow();
        LocalDate today = LocalDate.now();
        // Default: last 1 month (minimum retention)
        LocalDate startDate = from != null ? from : today.minusMonths(1);
        LocalDate endDate = to != null ? to : today;
        if (startDate.isAfter(endDate)) {
            startDate = endDate.minusMonths(1);
        }
        List<Attendance> records = attendanceService.getAttendanceForReports(startDate, endDate);

        model.addAttribute("inCharge", inCharge);
        model.addAttribute("records", records);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        return "reports";
    }
}
