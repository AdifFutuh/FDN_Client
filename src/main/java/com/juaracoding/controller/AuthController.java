package com.juaracoding.controller;

import com.juaracoding.dto.reference.RefAccessDTO;
import com.juaracoding.dto.validation.ValUserDTO;
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
            WebRequest webRequest
    ){
        ResponseEntity<Object> response = null;
        String menuNavBar = "";
        try{
            ValUserDTO userDTO = new ValUserDTO();
            userDTO.setNama(valUserDTO.getNama());
            userDTO.setEmail(valUserDTO.getEmail());
            userDTO.setPassword(valUserDTO.getPassword());
            userDTO.setAlamat(valUserDTO.getAlamat());
            userDTO.setTanggalLahir(valUserDTO.getTanggalLahir());
            userDTO.setNoHp(valUserDTO.getNoHp());
            userDTO.setUsername(valUserDTO.getUsername());

            RefAccessDTO accessDTO = new RefAccessDTO();
            accessDTO.setId(3L);
            valUserDTO.setAkses(accessDTO);
            response = authService.regis(valUserDTO);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return ConstantPage.VERIFY_OTP;
    }

}