package com.juaracoding.controller;

import com.juaracoding.dto.reference.RefCourseDTO;
import com.juaracoding.dto.reference.RefDetailCourseDTO;
import com.juaracoding.dto.reference.RefUserDTO;
import com.juaracoding.dto.validation.ValCourseDTO;
import com.juaracoding.dto.validation.ValDetailCourseDTO;
import com.juaracoding.dto.validation.ValDetailCourseForm;
import com.juaracoding.dto.validation.ValMapUserDetailCourseDTO;
import com.juaracoding.httpservice.CourseService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
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
            redirectAttributes.addFlashAttribute("successMessage", "Menambahkan Kursus "+courseDTO.getNama() +" Kedalam List! ");
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

            Integer idCourse = detailCourseForm.getIdCourse();
            redirectAttributes.addFlashAttribute("successMessage", "menambahkan detail course.");
            return "redirect:/detail-course-manage/" + courseName + "?page=" + page + "&idCourse=" + idCourse;
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Gagal menambahkan detail course.");
            Integer idCourse = detailCourseForm.getIdCourse();

            return "redirect:/detail-course-manage/" + courseName + "?page=" + page + "&idCourse=" + idCourse;
        }
    }

    @PostMapping("/submit-progress/{idDetailCourse}")
    public String submitProgressFromUser(
            @PathVariable("idDetailCourse") long idDetailCourse,
            @ModelAttribute("mapUserDetailCourseDTO") ValMapUserDetailCourseDTO mapUserDetailCourseDTO,
            RedirectAttributes redirectAttributes,
            HttpServletRequest request
    ) {
        Long idUser = (Long) request.getSession().getAttribute("USR_ID");
        if (idUser == null) {
            redirectAttributes.addFlashAttribute("error", "User belum login.");
            return "redirect:/login";
        }

        // Inject data user dan course
        RefUserDTO userDTO = new RefUserDTO(idUser);
        RefDetailCourseDTO detailCourseDTO = new RefDetailCourseDTO(idDetailCourse);
        mapUserDetailCourseDTO.setUser(userDTO);
        mapUserDetailCourseDTO.setDetailCourse(detailCourseDTO);
        try {
            ResponseEntity<Object> response = courseService.addProgress(idUser, idDetailCourse, mapUserDetailCourseDTO);
            redirectAttributes.addFlashAttribute("successMessage", "Progres Berhasul dikirim.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Terjadi kesalahan saat mengirim progress: " + e.getMessage());
        }

        // Redirect ke halaman profil user
        return "redirect:/users/profile/" + idUser;
    }

}
