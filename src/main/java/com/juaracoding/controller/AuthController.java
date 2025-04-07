package com.juaracoding.controller;

import com.juaracoding.dto.reference.RefAccessDTO;
import com.juaracoding.dto.validation.ValUserDTO;
import com.juaracoding.dto.validation.ValVerifyRegisDTO;
import com.juaracoding.httpservice.AuthService;
import com.juaracoding.utils.ConstantPage;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDate;
import java.util.Map;

@Controller
@RequestMapping("auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping("/verify-regis")
    public String verifyRegis(
            Model model,
            @Valid @ModelAttribute("userDTO") ValVerifyRegisDTO verifyRegisDTO,
            BindingResult result,
            WebRequest webRequest
    ){
        ResponseEntity<Object> response = null;
        String menuNavBar = "";
        try{

            response = authService.verifyRegis(verifyRegisDTO);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return ConstantPage.HOME_PAGE;
    }

    @PostMapping("/regis")
    public String regis(
            Model model,
            @Valid @ModelAttribute("verifyRegisDTO") ValUserDTO valUserDTO,
            BindingResult result,
            WebRequest webRequest
    ){
        String menuNavBar = "";
        try{
            RefAccessDTO accessDTO = new RefAccessDTO();
            accessDTO.setId(3L);
            valUserDTO.setAkses(accessDTO);
            ResponseEntity<Object> response = authService.regis(valUserDTO);
            Map<String,Object> responseBody = (Map<String, Object>) response.getBody();

            boolean success = (Boolean) responseBody.get("success");
            String errorCode = (String)responseBody.get("error-code");

            if (success){
                ValVerifyRegisDTO verifyRegisDTO = new ValVerifyRegisDTO();
                verifyRegisDTO.setEmail(valUserDTO.getEmail());

                model.addAttribute("verifyRegisDTO", verifyRegisDTO);
                return ConstantPage.VERIFY_OTP;
            }

            if (errorCode.equals("authFVReg001")){
                System.out.println("masuk sini");
                return ConstantPage.LOGIN_PAGE;
            }
            if (errorCode.equals("authFVReg002")){
                return ConstantPage.HOME_PAGE;
            }
            if (errorCode.equals("authFVReg003")){
                return ConstantPage.SUCCESS_PAGE;
            }


        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return "redirect:/register";
    }

}