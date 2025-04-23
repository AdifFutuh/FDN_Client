package com.juaracoding.controller;

import com.juaracoding.dto.reference.RefCourseDTO;
import com.juaracoding.dto.reference.RefUserDTO;
import com.juaracoding.dto.validation.ValCourseDTO;
import com.juaracoding.dto.validation.ValUserCourseProgressDTO;
import com.juaracoding.dto.validation.ValUserCourseProgressForm;
import com.juaracoding.httpservice.MapUserCourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("user-course")
public class MapUserCourseController {

    @Autowired
    private MapUserCourseService mapUserCourseService;

    @PostMapping
    public String addMapUserCourse(
            @ModelAttribute("userCourseProgressForm") ValUserCourseProgressForm userCourseProgressForm,
            RedirectAttributes redirectAttributes
    ){
        try {
            RefUserDTO refUserDTO = new RefUserDTO(userCourseProgressForm.getUserId());
            RefCourseDTO refCourseDTO = new RefCourseDTO(userCourseProgressForm.getCourseId());
            ValUserCourseProgressDTO  userCourseProgressDTO = new ValUserCourseProgressDTO();
            userCourseProgressDTO.setUser(refUserDTO);
            userCourseProgressDTO.setCourse(refCourseDTO);
            mapUserCourseService.addUserCourse(userCourseProgressDTO);
            return "redirect:/user-list";
        }catch (Exception e){
            return "redirect:/user-list";
        }
    }
}
