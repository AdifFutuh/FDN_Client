package com.juaracoding.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.juaracoding.dto.reference.RefAccessDTO;
import com.juaracoding.dto.response.ErrorResponseDTO;
import com.juaracoding.dto.validation.ValUserDTO;
import com.juaracoding.dto.validation.ValVerifyRegisDTO;
import com.juaracoding.httpservice.AuthService;
import com.juaracoding.utils.ConstantPage;
import feign.FeignException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.WebRequest;

import java.io.IOException;
import java.util.Map;

@Controller
@RequestMapping("auth")
public class AuthController {
    @Autowired
    private AuthService authService;


    @PostMapping("/regis")
    public String regis(
            Model model,
            @Valid @ModelAttribute("verifyRegisDTO") ValUserDTO valUserDTO,
            BindingResult result,
            WebRequest webRequest
    ) {
        String menuNavBar = "";
        try {
            RefAccessDTO accessDTO = new RefAccessDTO();
            accessDTO.setId(3L);
            valUserDTO.setAkses(accessDTO);
            ResponseEntity<Object> response = authService.regis(valUserDTO);
            Map<String, Object> responseBody = (Map<String, Object>) response.getBody();

            boolean success = (Boolean) responseBody.get("success");

            if (success) {
                ValVerifyRegisDTO verifyRegisDTO = new ValVerifyRegisDTO();
                verifyRegisDTO.setEmail(valUserDTO.getEmail());

                model.addAttribute("verifyRegisDTO", verifyRegisDTO);
                return ConstantPage.VERIFY_OTP;
            }


        } catch (FeignException e) {
            try {
                String responseJson = e.contentUTF8(); // Ambil isi body
                ObjectMapper objectMapper = new ObjectMapper();
                ErrorResponseDTO errorResponse = objectMapper.readValue(responseJson, ErrorResponseDTO.class);

                String errorCode = errorResponse.getErrorCode();
                String message = errorResponse.getMessage();
                boolean success = errorResponse.isSuccess();

                if ("authFVReg001".equals(errorCode)) {
                    model.addAttribute("error", message);
                    model.addAttribute("userDTO", new ValUserDTO());
                    return ConstantPage.REGIS_PAGE;
                }
                if ("authFVReg002".equals(errorCode)) {
                    return ConstantPage.SUCCESS_PAGE;
                }
                if ("authFVReg003".equals(errorCode)) {
                    model.addAttribute("error", "Email Sudah terdaftar silahkan verifikasi OTP dahulu");
                    return ConstantPage.VERIFY_OTP;
                }else{
                    model.addAttribute("title", "Terjadi kesalahan");
                    model.addAttribute("message", message);
                    model.addAttribute("buttonText", "Ke Beranda");
                    model.addAttribute("success", success);
                    return ConstantPage.ERROR_PAGE;
                }

            } catch (IOException jsonException) {
                jsonException.printStackTrace();
            }
        }
        return "redirect:/register";
    }


    @PostMapping("/verify-regis")
    public String verifyRegis(
            Model model,
            @Valid @ModelAttribute("userDTO") ValVerifyRegisDTO verifyRegisDTO,
            BindingResult result,
            WebRequest webRequest
    ) {
        String menuNavBar = "";
        try {
            ResponseEntity<Object> response = authService.verifyRegis(verifyRegisDTO);

            Map<String, Object> responseBody = (Map<String, Object>) response.getBody();

            boolean success = (Boolean) responseBody.get("success");
            model.addAttribute("title", "Akun Teregistrasi");
            model.addAttribute("message", "Selamat Akun Anda Berhasil DiBuat!!");
            model.addAttribute("buttonText", "Ke Beranda");
            model.addAttribute("success", success);
        } catch (FeignException e) {
            try {
                String responseJson = e.contentUTF8();
                ObjectMapper objectMapper = new ObjectMapper();
                Map<String, Object> responseBody = objectMapper.readValue(responseJson, new TypeReference<Map<String, Object>>() {
                });

                String errorCode = (String) responseBody.get("error-code");
                model.addAttribute("error", responseBody.get("message"));

                if ("authFVReg001".equals(errorCode)) {
                    return ConstantPage.LOGIN_PAGE;
                }
                if ("authFVReg002".equals(errorCode)) {
                    return ConstantPage.SUCCESS_PAGE;
                }
                if ("authFVReg003".equals(errorCode)) {
                    return ConstantPage.SUCCESS_PAGE;
                }
            } catch (IOException jsonException) {
                jsonException.printStackTrace();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return ConstantPage.SUCCESS_PAGE;
    }

}