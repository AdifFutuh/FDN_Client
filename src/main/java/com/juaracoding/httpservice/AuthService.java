package com.juaracoding.httpservice;

import com.juaracoding.config.FeignClientConfig;
import com.juaracoding.dto.validation.ValLoginDTO;
import com.juaracoding.dto.validation.ValUserDTO;
import com.juaracoding.dto.validation.ValVerifyRegisDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@FeignClient(name = "auth-services", configuration = FeignClientConfig.class, url = "http://localhost:8080/auth")
public interface AuthService {

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody ValLoginDTO valLoginDTO);

    @PostMapping("/regis")
    public ResponseEntity<Object> regis(@RequestBody ValUserDTO regisDTO);


    @PostMapping("/verify-regis")
    public ResponseEntity<Object> verifyRegis(@Valid @RequestBody ValVerifyRegisDTO verifyRegisDTO);

    @PostMapping("/resend-otp")
    ResponseEntity<Object> resendOtp(@RequestParam("email") String email);

}