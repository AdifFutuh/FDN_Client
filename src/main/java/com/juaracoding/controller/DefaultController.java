package com.juaracoding.controller;

import com.juaracoding.dto.validation.ValUserDTO;
import com.juaracoding.utils.ConstantPage;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class DefaultController {
    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("userDTO", new ValUserDTO());
        return ConstantPage.REGIS_PAGE;
    }
}
