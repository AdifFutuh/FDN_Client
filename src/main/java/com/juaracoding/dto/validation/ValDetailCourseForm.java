package com.juaracoding.dto.validation;

import com.juaracoding.dto.reference.RefCourseDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ValDetailCourseForm {
    private String judul;
    private String content;
    private Integer urutan;
    private Integer idCourse;
}
