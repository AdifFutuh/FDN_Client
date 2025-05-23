package com.juaracoding.dto.validation;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class ValVerifyRegisDTO implements Serializable {
    @Pattern(regexp = "^([0-9]{6})$", message = "OTP wajib 6 digit angka")
    private String otp;

    @NotNull
    @NotEmpty
    @NotBlank
    private String email;
}
