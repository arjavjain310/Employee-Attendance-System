package com.attendance.controller;

import com.attendance.dto.RegisterDto;
import com.attendance.entity.InCharge;
import com.attendance.service.InChargeService;
import jakarta.validation.Valid;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/register")
public class RegistrationController {

    private final InChargeService inChargeService;
    private final PasswordEncoder passwordEncoder;

    public RegistrationController(InChargeService inChargeService, PasswordEncoder passwordEncoder) {
        this.inChargeService = inChargeService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public String registerPage(Model model) {
        model.addAttribute("registerDto", new RegisterDto());
        return "register";
    }

    @PostMapping
    public String register(@Valid @ModelAttribute("registerDto") RegisterDto dto, BindingResult result,
                           RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "register";
        }
        if (inChargeService.existsByUserId(dto.getUserId())) {
            result.rejectValue("userId", "error.userId", "User ID already registered");
            return "register";
        }
        InCharge inCharge = new InCharge();
        inCharge.setUserId(dto.getUserId().trim());
        inCharge.setPassword(passwordEncoder.encode(dto.getPassword()));
        inCharge.setName(dto.getName().trim());
        inChargeService.register(inCharge);
        redirectAttributes.addFlashAttribute("success", "Registration successful. Please log in.");
        return "redirect:/login";
    }
}
