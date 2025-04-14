package com.juaracoding.controller;

import com.juaracoding.dto.validation.ValCourseDTO;
import com.juaracoding.httpservice.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("course")
public class CourseController {
    @Autowired
    private CourseService courseService;

    @PostMapping
    public String addCourse(
            @ModelAttribute("courseDTO")ValCourseDTO courseDTO,
            RedirectAttributes redirectAttributes
    ){
        try {
            courseService.addCourse(courseDTO);
            return "redirect:/courses";
        }catch (Exception e){
            return "redirect:/courses";
        }
    }
}
