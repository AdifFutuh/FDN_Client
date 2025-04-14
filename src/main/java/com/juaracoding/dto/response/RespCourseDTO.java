package com.juaracoding.dto.response;

import lombok.Data;

@Data
public class RespCourseDTO {

    private long id;

    private String nama;

    private String deskripsi;

    private long jumlahSiswa;
}