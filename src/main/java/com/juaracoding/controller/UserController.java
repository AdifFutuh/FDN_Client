package com.juaracoding.controller;

import com.juaracoding.dto.response.RespMapUserDetailCourseDTO;
import com.juaracoding.dto.response.RespUserCourseProgressDTO;
import com.juaracoding.dto.response.RespUserProfile;
import com.juaracoding.httpservice.DashboardService;
import com.juaracoding.httpservice.UserService;
import com.juaracoding.utils.ConstantPage;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private DashboardService dashboardService;

    @GetMapping("/profile/{id}")
    public String showUserProfile(@PathVariable("id") Long id, Model model, HttpServletRequest request) {
        String user = (String) request.getSession().getAttribute("USR_NAME");
        String menuNavbar = (String) request.getSession().getAttribute("MENU_NAVBAR");

        model.addAttribute("USR_NAME", user);
        model.addAttribute("MENU_NAVBAR", menuNavbar);
        ResponseEntity<Object> responseEntity = dashboardService.dashboardPeserta(id);

        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            Map<String, Object> body = (LinkedHashMap<String, Object>) responseEntity.getBody();
            LinkedHashMap<String, Object> dataMap = (LinkedHashMap<String, Object>) body.get("data");

            // Konversi manual ke RespUserProfile
            RespUserProfile profile = new RespUserProfile();
            profile.setId(Long.valueOf(dataMap.get("id").toString()));
            profile.setUsername((String) dataMap.get("username"));
            profile.setEmail((String) dataMap.get("email"));
            profile.setAlamat((String) dataMap.get("alamat"));
            profile.setNoHp((String) dataMap.get("noHp"));
            profile.setNama((String) dataMap.get("nama"));
            profile.setTanggalLahir(java.time.LocalDate.parse((String) dataMap.get("tanggalLahir")));

            // Parsing list course progress
            List<RespUserCourseProgressDTO> courseProgressList = new ArrayList<>();
            List<LinkedHashMap<String, Object>> progressList = (List<LinkedHashMap<String, Object>>) dataMap.get("courseProgressList");

            for (LinkedHashMap<String, Object> progress : progressList) {
                RespUserCourseProgressDTO dto = new RespUserCourseProgressDTO();
                dto.setCourseId(Long.valueOf(progress.get("courseId").toString()));
                dto.setCourseName((String) progress.get("courseName"));
                dto.setPersentase(Double.parseDouble(progress.get("persentase").toString()));
                dto.setCreatedDate(java.time.LocalDateTime.parse((String) progress.get("createdDate")));
                dto.setUpdateTime(java.time.LocalDateTime.parse((String) progress.get("updateTime")));
                courseProgressList.add(dto);
            }
            profile.setCourseProgressList(courseProgressList);

            // Parsing list user detail course
            List<RespMapUserDetailCourseDTO> userDetailCourseDTOS = new ArrayList<>();
            List<LinkedHashMap<String, Object>> userDetailCourseList = (List<LinkedHashMap<String, Object>>) dataMap.get("userDetailCourseList");

            for (LinkedHashMap<String, Object> udc : userDetailCourseList) {
                RespMapUserDetailCourseDTO dto = new RespMapUserDetailCourseDTO();
                dto.setId(Long.valueOf(udc.get("id").toString()));
                dto.setUsername((String) udc.get("username"));
                dto.setNamaCourse((String) udc.get("namaCourse"));
                dto.setJudulDetailCourse((String) udc.get("judulDetailCourse"));
                dto.setSummary((String) udc.get("summary"));
                dto.setStatus((String) udc.get("status"));
                userDetailCourseDTOS.add(dto);
            }
            profile.setUserDetailCourseList(userDetailCourseDTOS);

            model.addAttribute("userProfile", profile);
            return ConstantPage.PROFILE_PAGE;
        } else {
            model.addAttribute("errorMessage", "Gagal mengambil data profil.");
            return "error"; // fallback page
        }
    }

    @GetMapping("/profile")
    public String showMyProfile( Model model, HttpServletRequest request) {
        String user = (String) request.getSession().getAttribute("USR_NAME");
        String menuNavbar = (String) request.getSession().getAttribute("MENU_NAVBAR");
        Long idUser = (Long) request.getSession().getAttribute("USR_ID");

        model.addAttribute("USR_NAME", user);
        model.addAttribute("MENU_NAVBAR", menuNavbar);
        ResponseEntity<Object> responseEntity = dashboardService.dashboardPeserta(idUser);

        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            Map<String, Object> body = (LinkedHashMap<String, Object>) responseEntity.getBody();
            LinkedHashMap<String, Object> dataMap = (LinkedHashMap<String, Object>) body.get("data");

            // Konversi manual ke RespUserProfile
            RespUserProfile profile = new RespUserProfile();
            profile.setId(Long.valueOf(dataMap.get("id").toString()));
            profile.setUsername((String) dataMap.get("username"));
            profile.setEmail((String) dataMap.get("email"));
            profile.setAlamat((String) dataMap.get("alamat"));
            profile.setNoHp((String) dataMap.get("noHp"));
            profile.setNama((String) dataMap.get("nama"));
            profile.setTanggalLahir(java.time.LocalDate.parse((String) dataMap.get("tanggalLahir")));

            // Parsing list course progress
            List<RespUserCourseProgressDTO> courseProgressList = new ArrayList<>();
            List<LinkedHashMap<String, Object>> progressList = (List<LinkedHashMap<String, Object>>) dataMap.get("courseProgressList");

            for (LinkedHashMap<String, Object> progress : progressList) {
                RespUserCourseProgressDTO dto = new RespUserCourseProgressDTO();
                dto.setCourseId(Long.valueOf(progress.get("courseId").toString()));
                dto.setCourseName((String) progress.get("courseName"));
                dto.setPersentase(Double.parseDouble(progress.get("persentase").toString()));
                dto.setCreatedDate(java.time.LocalDateTime.parse((String) progress.get("createdDate")));
                dto.setUpdateTime(java.time.LocalDateTime.parse((String) progress.get("updateTime")));
                courseProgressList.add(dto);
            }
            profile.setCourseProgressList(courseProgressList);

            // Parsing list user detail course
            List<RespMapUserDetailCourseDTO> userDetailCourseDTOS = new ArrayList<>();
            List<LinkedHashMap<String, Object>> userDetailCourseList = (List<LinkedHashMap<String, Object>>) dataMap.get("userDetailCourseList");

            for (LinkedHashMap<String, Object> udc : userDetailCourseList) {
                RespMapUserDetailCourseDTO dto = new RespMapUserDetailCourseDTO();
                dto.setId(Long.valueOf(udc.get("id").toString()));
                dto.setUsername((String) udc.get("username"));
                dto.setNamaCourse((String) udc.get("namaCourse"));
                dto.setJudulDetailCourse((String) udc.get("judulDetailCourse"));
                dto.setSummary((String) udc.get("summary"));
                dto.setStatus((String) udc.get("status"));
                userDetailCourseDTOS.add(dto);
            }
            profile.setUserDetailCourseList(userDetailCourseDTOS);

            model.addAttribute("userProfile", profile);
            return ConstantPage.PROFILE_PAGE;
        } else {
            model.addAttribute("errorMessage", "Gagal mengambil data profil.");
            return "error"; // fallback page
        }
    }

}
