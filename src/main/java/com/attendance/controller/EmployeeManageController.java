package com.attendance.controller;

import com.attendance.dto.AddEmployeeDto;
import com.attendance.entity.Employee;
import com.attendance.entity.InCharge;
import com.attendance.service.EmployeeService;
import com.attendance.service.InChargeService;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/employees")
public class EmployeeManageController {

    private final InChargeService inChargeService;
    private final EmployeeService employeeService;

    public EmployeeManageController(InChargeService inChargeService, EmployeeService employeeService) {
        this.inChargeService = inChargeService;
        this.employeeService = employeeService;
    }

    @GetMapping("/manage")
    public String managePage(@AuthenticationPrincipal User user, Model model) {
        InCharge inCharge = inChargeService.findByUserId(user.getUsername()).orElseThrow();
        model.addAttribute("inCharge", inCharge);
        model.addAttribute("employees", employeeService.findAll());
        model.addAttribute("addEmployeeDto", new AddEmployeeDto());
        return "employees-manage";
    }

    @PostMapping("/add")
    public String addEmployee(
            @AuthenticationPrincipal User user,
            @Valid @ModelAttribute("addEmployeeDto") AddEmployeeDto dto,
            BindingResult result,
            RedirectAttributes redirectAttributes,
            Model model) {
        InCharge inCharge = inChargeService.findByUserId(user.getUsername()).orElseThrow();
        if (result.hasErrors()) {
            model.addAttribute("inCharge", inCharge);
            model.addAttribute("employees", employeeService.findAll());
            return "employees-manage";
        }
        String code = dto.getEmployeeCode().trim();
        if (employeeService.existsByEmployeeCode(code)) {
            result.rejectValue("employeeCode", "error.employeeCode", "Employee code already exists");
            model.addAttribute("inCharge", inCharge);
            model.addAttribute("employees", employeeService.findAll());
            return "employees-manage";
        }
        employeeService.addEmployee(code, dto.getName());
        redirectAttributes.addFlashAttribute("success", "Employee added successfully.");
        return "redirect:/employees/manage";
    }

    @PostMapping("/remove")
    public String removeEmployee(
            @AuthenticationPrincipal User user,
            @RequestParam Long id,
            RedirectAttributes redirectAttributes) {
        Employee employee = employeeService.findById(id).orElse(null);
        if (employee != null) {
            employeeService.removeEmployee(id);
            redirectAttributes.addFlashAttribute("success", "Employee '" + employee.getName() + "' marked as left (removed from active list).");
        } else {
            redirectAttributes.addFlashAttribute("error", "Employee not found.");
        }
        return "redirect:/employees/manage";
    }
}
