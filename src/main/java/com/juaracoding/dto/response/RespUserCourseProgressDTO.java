package com.juaracoding.dto.response;

import lombok.Data;

import java.time.LocalDate;

@Data
public class RespUserCourseProgressDTO {
    private long id;

    private String username;

    private String email;

    private String alamat;

    private String noHp;

    private String nama;

    private LocalDate tanggalLahir;

    private String namaCourse;

    private double progres;
}
