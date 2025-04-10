package com.juaracoding.controller;

import com.juaracoding.dto.validation.ValLoginDTO;
import com.juaracoding.dto.validation.ValUserDTO;
import com.juaracoding.dto.validation.ValVerifyRegisDTO;
import com.juaracoding.utils.ConstantPage;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class DefaultController {

    @GetMapping
    public String indexPage(Model model){
        model.addAttribute("x","");
        return ConstantPage.HOME_PAGE;
    }

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("userDTO", new ValUserDTO());
        return ConstantPage.REGIS_PAGE;
    }

    @GetMapping("/verify-regis")
    public String goToVerifyRegis(Model model) {
        return ConstantPage.VERIFY_OTP;
    }

    @GetMapping("/success-regis")
    public String goToSuccessRegis(Model model){
        return ConstantPage.SUCCESS_PAGE;
    }

    @GetMapping("/home")
    public String goToHome(Model model) {
        return ConstantPage.HOME_PAGE;
    }

    @GetMapping("/login")
    public String goToLogin(Model model) {
        model.addAttribute("loginDTO", new ValLoginDTO());
        return ConstantPage.LOGIN_PAGE;
    }
}