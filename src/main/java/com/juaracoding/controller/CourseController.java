package com.juaracoding.controller;

import com.juaracoding.dto.reference.RefCourseDTO;
import com.juaracoding.dto.validation.ValCourseDTO;
import com.juaracoding.dto.validation.ValDetailCourseDTO;
import com.juaracoding.dto.validation.ValDetailCourseForm;
import com.juaracoding.httpservice.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    @PostMapping("/add-detail-course")
    private String addDetailCourse(
            @ModelAttribute("detailCourseDTO") ValDetailCourseForm detailCourseForm,
            @RequestParam("courseName") String courseName,
            @RequestParam("page") Integer page,
            RedirectAttributes redirectAttributes
    ) {
        try {
            ValDetailCourseDTO detailCourseDTO = new ValDetailCourseDTO();
            RefCourseDTO courseDTO = new RefCourseDTO(detailCourseForm.getIdCourse());
            detailCourseDTO.setJudul(detailCourseForm.getJudul());
            detailCourseDTO.setUrutan(detailCourseForm.getUrutan());
            detailCourseDTO.setContent(detailCourseForm.getContent());
            detailCourseDTO.setCourse(courseDTO);

            courseService.addDetailCourse(detailCourseDTO);

            // Ambil ID course dari form dan masukkan ke URL redirect
            Integer idCourse = detailCourseForm.getIdCourse();

            return "redirect:/detail-course-manage/" + courseName + "?page=" + page + "&idCourse=" + idCourse;
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Gagal menambahkan detail course.");
            Integer idCourse = detailCourseForm.getIdCourse();

            return "redirect:/detail-course-manage/" + courseName + "?page=" + page + "&idCourse=" + idCourse;
        }
    }


}
