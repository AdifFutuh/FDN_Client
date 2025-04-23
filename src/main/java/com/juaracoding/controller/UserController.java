package com.juaracoding.controller;

import com.juaracoding.dto.response.RespUserCourseProgressDTO;
import com.juaracoding.dto.response.RespUserProfile;
import com.juaracoding.httpservice.DashboardService;
import com.juaracoding.httpservice.UserService;
import com.juaracoding.utils.ConstantPage;
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
    public String showUserProfile(@PathVariable("id") Long id, Model model) {
        ResponseEntity<Object> responseEntity = dashboardService.dashboardPeserta(id);

        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            // casting hasil responseEntity agar dapat mengambil field "data"
            Map<String, Object> body = (LinkedHashMap<String, Object>) responseEntity.getBody();
            LinkedHashMap<String, Object> dataMap = (LinkedHashMap<String, Object>) body.get("data");

            // konversi manual ke RespUserProfile (atau bisa gunakan ObjectMapper)
            RespUserProfile profile = new RespUserProfile();
            profile.setId(Long.valueOf(dataMap.get("id").toString()));
            profile.setUsername((String) dataMap.get("username"));
            profile.setEmail((String) dataMap.get("email"));
            profile.setAlamat((String) dataMap.get("alamat"));
            profile.setNoHp((String) dataMap.get("noHp"));
            profile.setNama((String) dataMap.get("nama"));
            profile.setTanggalLahir(java.time.LocalDate.parse((String) dataMap.get("tanggalLahir")));

            // progressList parsing manual
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

            model.addAttribute("userProfile", profile);
            return ConstantPage.PROFILE_PAGE;
        } else {
            model.addAttribute("errorMessage", "Gagal mengambil data profil.");
            return "error"; // halaman error atau fallback
        }
    }
}
