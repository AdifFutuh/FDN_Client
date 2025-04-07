package com.juaracoding.dto.validation;


import com.juaracoding.dto.reference.RefCourseDTO;
import com.juaracoding.dto.reference.RefUserDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ValUserCourseProgressDTO {

    private RefUserDTO user;

    private RefCourseDTO course;
}
