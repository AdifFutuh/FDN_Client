package com.juaracoding.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.juaracoding.dto.reference.RefAccessDTO;
import com.juaracoding.dto.response.ErrorResponseDTO;
import com.juaracoding.dto.validation.ValLoginDTO;
import com.juaracoding.dto.validation.ValUserDTO;
import com.juaracoding.dto.validation.ValVerifyRegisDTO;
import com.juaracoding.httpservice.AuthService;
import com.juaracoding.utils.ConstantPage;
import com.juaracoding.utils.GenerateStringMenu;
import feign.FeignException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("auth")
public class AuthController {
    @Autowired
    private AuthService authService;


    @PostMapping("/regis")
    public String regis(
            Model model,
            @Valid @ModelAttribute("userDTO") ValUserDTO valUserDTO,
            BindingResult result,
            WebRequest webRequest,
            RedirectAttributes redirectAttributes
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

                redirectAttributes.addFlashAttribute("verifyRegisDTO", verifyRegisDTO);
                return "redirect:/verify-regis";
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
                    model.addAttribute("error", message);
                    model.addAttribute("userDTO", new ValUserDTO());
                    return ConstantPage.REGIS_PAGE;
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
            @Valid @ModelAttribute("verifyRegisDTO") ValVerifyRegisDTO verifyRegisDTO,
            BindingResult result,
            WebRequest webRequest,
            RedirectAttributes redirectAttributes

    ) {
        String menuNavBar = "";
        try {
            ResponseEntity<Object> response = authService.verifyRegis(verifyRegisDTO);

            Map<String, Object> responseBody = (Map<String, Object>) response.getBody();

            boolean success = (Boolean) responseBody.get("success");
            redirectAttributes.addFlashAttribute("title", "Akun Teregistrasi");
            redirectAttributes.addFlashAttribute("message", "Selamat Akun Anda Berhasil DiBuat!!");
            redirectAttributes.addFlashAttribute("buttonText", "Masuk");
            redirectAttributes.addFlashAttribute("buttonLink", "/login");
            redirectAttributes.addFlashAttribute("success", success);
        } catch (FeignException e) {
            try {
                String responseJson = e.contentUTF8();
                ObjectMapper objectMapper = new ObjectMapper();
                Map<String, Object> responseBody = objectMapper.readValue(responseJson, new TypeReference<Map<String, Object>>() {
                });

                String errorCode = (String) responseBody.get("error-code");
                redirectAttributes.addFlashAttribute("error", responseBody.get("message"));

                redirectAttributes.addFlashAttribute("verifyRegisDTO", verifyRegisDTO);

                if ("authFVOtp01".equals(errorCode)) {
                    redirectAttributes.addFlashAttribute("error", responseBody.get("message"));
                    return "redirect:/verify-regis";
                }
                if ("authFVOtp02".equals(errorCode)) {
                    redirectAttributes.addFlashAttribute("error", responseBody.get("message"));
                    return "redirect:/verify-regis";
                }
                if ("authFVOtp03".equals(errorCode)) {
                    redirectAttributes.addFlashAttribute("error", responseBody.get("message"));
                    return "redirect:/verify-regis";
                }
            } catch (IOException jsonException) {
                jsonException.printStackTrace();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return "redirect:/success-regis";
    }


    @PostMapping("/resend-otp")
    public String resendOtp(
            Model model,
            @ModelAttribute("verifyRegisDTO") ValVerifyRegisDTO verifyRegisDTO,
            WebRequest webRequest,
            RedirectAttributes redirectAttributes

    ) {
        try {
            ResponseEntity<Object> response = authService.resendOtp(verifyRegisDTO.getEmail());

            Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
            boolean success = (Boolean) responseBody.get("success");

            model.addAttribute("success", success);
            model.addAttribute("verifyRegisDTO", verifyRegisDTO); // penting biar email tetap terisi
            model.addAttribute("info", "Kode OTP berhasil dikirim ulang ke email.");

        } catch (FeignException e) {
            model.addAttribute("error", "Gagal mengirim ulang OTP. Coba beberapa saat lagi.");
            model.addAttribute("verifyRegisDTO", verifyRegisDTO);
        }

        redirectAttributes.addFlashAttribute("verifyRegisDTO", verifyRegisDTO);
        return "redirect:/verify-regis";
    }


    @PostMapping("/login")
    public String login(
            Model model,
            @ModelAttribute("loginDTO") ValLoginDTO loginDTO,
            WebRequest webRequest,
            HttpServletRequest request,
            RedirectAttributes redirectAttributes
    ) {
        try {
            ResponseEntity<Object> response = null;
            String tokenJwt = "";
            String menuNavBar = "";

            try {
                response = authService.login(loginDTO);

                if (response.getStatusCode().is2xxSuccessful()) {
                    Map<String, Object> map = (Map<String, Object>) response.getBody();
                    Map<String, Object> data = (Map<String, Object>) map.get("data");
                    List<Map<String, Object>> ltMenu = (List<Map<String, Object>>) data.get("menu");
                    menuNavBar = new GenerateStringMenu().stringMenu(ltMenu);
                    tokenJwt = (String) data.get("token");

                    webRequest.setAttribute("MENU_NAVBAR", menuNavBar, WebRequest.SCOPE_SESSION);
                    webRequest.setAttribute("JWT", tokenJwt, WebRequest.SCOPE_SESSION);
                    webRequest.setAttribute("USR_NAME", loginDTO.getUsername(), WebRequest.SCOPE_SESSION);


                    return "redirect:/home";
                } else {
                    redirectAttributes.addFlashAttribute("error", "Login gagal: status tidak sukses.");
                    return "redirect:/login";
                }
            } catch (Exception e) {
                // Misal error karena username/password salah atau server error
                redirectAttributes.addFlashAttribute("error", "Login gagal: " + e.getMessage());
                return "redirect:/login";
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/logout")
    public String destroySession(HttpServletRequest request){
        request.getSession().invalidate();
        return "redirect:/";
    }

}