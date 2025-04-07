package com.juaracoding.dto.validation;


import com.juaracoding.dto.reference.RefDetailCourseDTO;
import com.juaracoding.dto.reference.RefUserDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ValMapUserDetailCourseDTO {
    private RefUserDTO user;

    private RefDetailCourseDTO detailCourse;

    private String summary;

}
