package com.juaracoding.controller;

import com.juaracoding.dto.response.RespCourseDTO;
import com.juaracoding.dto.response.RespDetailCourseDTO;
import com.juaracoding.dto.response.RespUserDTO;
import com.juaracoding.dto.validation.ValCourseDTO;
import com.juaracoding.dto.validation.ValDetailCourseDTO;
import com.juaracoding.dto.validation.ValLoginDTO;
import com.juaracoding.dto.validation.ValUserDTO;
import com.juaracoding.httpservice.CourseService;
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
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/")
public class DefaultController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private UserService userService;

    @GetMapping
    public String indexPage(HttpServletRequest request, Model model) {
        String user = (String) request.getSession().getAttribute("USR_NAME");
        String menuNavbar = (String) request.getSession().getAttribute("MENU_NAVBAR");
        model.addAttribute("USR_NAME", user);
        model.addAttribute("MENU_NAVBAR", menuNavbar);
        return "home";
    }

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("userDTO", new ValUserDTO());
        return ConstantPage.REGIS_PAGE;
    }

    @GetMapping("/verify-regis")
    public String goToVerifyRegis(Model model) {
        return ConstantPage.VERIFY_OTP;
    }

    @GetMapping("/success-regis")
    public String goToSuccessRegis(Model model) {
        return ConstantPage.SUCCESS_PAGE;
    }

    @GetMapping("/home")
    public String home(HttpServletRequest request, Model model) {
        String user = (String) request.getSession().getAttribute("USR_NAME");
        String menuNavbar = (String) request.getSession().getAttribute("MENU_NAVBAR");
        model.addAttribute("USR_NAME", user);
        model.addAttribute("MENU_NAVBAR", menuNavbar);
        return "home";
    }

    @GetMapping("/login")
    public String goToLogin(Model model) {
        model.addAttribute("loginDTO", new ValLoginDTO());
        return ConstantPage.LOGIN_PAGE;
    }

    @GetMapping("/user-list")
    public String goToUserList(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            HttpServletRequest request,
            Model model
    ) {
        String user = (String) request.getSession().getAttribute("USR_NAME");
        String menuNavbar = (String) request.getSession().getAttribute("MENU_NAVBAR");

        model.addAttribute("USR_NAME", user);
        model.addAttribute("MENU_NAVBAR", menuNavbar);

        // Feign call dengan parameter page
        ResponseEntity<Object> response = userService.findAllByParam("asc", "id", page, 5, "akses", "3");

        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
        boolean success = (Boolean) responseBody.get("success");

        if (success) {
            Map<String, Object> data = (Map<String, Object>) responseBody.get("data");
            List<Map<String, Object>> content = (List<Map<String, Object>>) data.get("content");

            List<RespUserDTO> userList = content.stream().map(item -> {
                RespUserDTO dto = new RespUserDTO();
                dto.setId(Long.parseLong(item.get("id").toString()));
                dto.setNama(item.get("nama").toString());
                dto.setEmail(item.get("email").toString());
                dto.setUsername(item.get("username").toString());
                dto.setAlamat(item.get("alamat").toString());
                dto.setTanggalLahir(LocalDate.parse(item.get("tanggalLahir").toString()));
                dto.setUmur(Integer.valueOf(item.get("umur").toString()));
                dto.setNoHp(item.get("noHp").toString());

                return dto;
            }).toList();

            model.addAttribute("userList", userList);
            model.addAttribute("totalPages", data.get("total-pages"));
            model.addAttribute("currentPage", data.get("current-page"));
        }

        return ConstantPage.LIST_USER;
    }

    @GetMapping("/courses")
    public String goToCourseList(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            HttpServletRequest request,
            Model model
    ) {
        String user = (String) request.getSession().getAttribute("USR_NAME");
        String menuNavbar = (String) request.getSession().getAttribute("MENU_NAVBAR");

        model.addAttribute("USR_NAME", user);
        model.addAttribute("MENU_NAVBAR", menuNavbar);

        // Feign call dengan parameter page
        ResponseEntity<Object> response = courseService.findAllCourse(page);

        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
        boolean success = (Boolean) responseBody.get("success");

        if (success) {
            Map<String, Object> data = (Map<String, Object>) responseBody.get("data");
            List<Map<String, Object>> content = (List<Map<String, Object>>) data.get("content");

            List<RespCourseDTO> courseList = content.stream().map(item -> {
                RespCourseDTO dto = new RespCourseDTO();
                dto.setId(Long.valueOf(item.get("id").toString()));
                dto.setNama(item.get("nama").toString());
                dto.setDeskripsi(item.get("deskripsi").toString());
                dto.setJumlahSiswa(Integer.parseInt(item.get("jumlahSiswa").toString()));
                return dto;
            }).toList();

            ValCourseDTO course = new ValCourseDTO();
            model.addAttribute("courseList", courseList);
            model.addAttribute("totalPages", data.get("total-pages"));
            model.addAttribute("currentPage", data.get("current-page"));
            model.addAttribute("courseDTO", course);
        }

        return ConstantPage.LIST_COURSE;
    }


    @GetMapping("/detail-course-manage/{courseName}")
    public String goToDetailCourseListById(
            @PathVariable("courseName") String courseName,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            HttpServletRequest request,
            Model model
    ) {
        String user = (String) request.getSession().getAttribute("USR_NAME");
        String menuNavbar = (String) request.getSession().getAttribute("MENU_NAVBAR");

        model.addAttribute("USR_NAME", user);
        model.addAttribute("MENU_NAVBAR", menuNavbar);
        // Feign call dengan parameter page
        ResponseEntity<Object> response = courseService.findDetailCourseByCourse(page, 10, "course", courseName);

        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
        boolean success = (Boolean) responseBody.get("success");

        if (success) {
            Map<String, Object> data = (Map<String, Object>) responseBody.get("data");
            List<Map<String, Object>> content = (List<Map<String, Object>>) data.get("detail-course");

            List<RespDetailCourseDTO> detailCourseList = content.stream().map(item -> {
                RespDetailCourseDTO dto = new RespDetailCourseDTO();
                dto.setId(Long.valueOf(item.get("id").toString()));
                dto.setJudul(item.get("judul").toString());
                dto.setContent(item.get("content").toString());
                dto.setUrutan(Integer.parseInt(item.get("urutan").toString()));
                return dto;
            }).toList();

            ValDetailCourseDTO detailCourse = new ValDetailCourseDTO();
            model.addAttribute("detailCourseList", detailCourseList);
            model.addAttribute("courseId", data.get("id-course"));
            model.addAttribute("totalPages", data.get("total-pages"));
            model.addAttribute("currentPage", data.get("current-page"));
            model.addAttribute("detailCourseDTO", detailCourse);
        }

        return ConstantPage.LIST_DETAIL_COURSE;
    }
}