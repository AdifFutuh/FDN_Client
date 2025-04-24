package com.juaracoding.controller;

import com.juaracoding.dto.reference.RefCourseDTO;
import com.juaracoding.dto.response.RespCourseDTO;
import com.juaracoding.dto.response.RespDetailCourseDTO;
import com.juaracoding.dto.response.RespUserDTO;
import com.juaracoding.dto.validation.*;
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
import java.util.stream.Collectors;

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
        return ConstantPage.HOME_PAGE;
    }

    @GetMapping("/contact-us")
    public String contactUs(HttpServletRequest request, Model model){
        String user = (String) request.getSession().getAttribute("USR_NAME");
        String menuNavbar = (String) request.getSession().getAttribute("MENU_NAVBAR");
        model.addAttribute("USR_NAME", user);
        model.addAttribute("MENU_NAVBAR", menuNavbar);
        return ConstantPage.CONTACT_US;
    }

    @GetMapping("about-us")
    public String aboutUs(HttpServletRequest request, Model model){
        String user = (String) request.getSession().getAttribute("USR_NAME");
        String menuNavbar = (String) request.getSession().getAttribute("MENU_NAVBAR");
        model.addAttribute("USR_NAME", user);
        model.addAttribute("MENU_NAVBAR", menuNavbar);
        return ConstantPage.ABOUT_US;
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

        // Ambil data user
        ResponseEntity<Object> response = userService.findAllByParam("asc", "id", page, 5, "akses", "3");

        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
        boolean success = Boolean.TRUE.equals(responseBody.get("success"));

        if (success && responseBody.containsKey("data")) {
            Map<String, Object> data = (Map<String, Object>) responseBody.get("data");
            List<Map<String, Object>> content = (List<Map<String, Object>>) data.get("content");

            List<RespUserDTO> userList = content.stream().map(item -> {
                RespUserDTO dto = new RespUserDTO();
                dto.setId(item.get("id") != null ? Long.parseLong(item.get("id").toString()) : 0L);
                dto.setNama(item.get("nama") != null ? item.get("nama").toString() : "");
                dto.setEmail(item.get("email") != null ? item.get("email").toString() : "");
                dto.setUsername(item.get("username") != null ? item.get("username").toString() : "");
                dto.setAlamat(item.get("alamat") != null ? item.get("alamat").toString() : "");
                dto.setTanggalLahir(item.get("tanggalLahir") != null ? LocalDate.parse(item.get("tanggalLahir").toString()) : null);
                dto.setNoHp(item.get("noHp") != null ? item.get("noHp").toString() : "");
                return dto;
            }).toList();

            model.addAttribute("userList", userList);
            model.addAttribute("totalPages", data.get("total-pages"));
            model.addAttribute("currentPage", data.get("current-page"));
        }

        // Ambil data course
        ResponseEntity<Object> response2 = courseService.findAllCourseAsAdmin();
        Map<String, Object> responseBody2 = (Map<String, Object>) response2.getBody();
        boolean success2 = Boolean.TRUE.equals(responseBody2.get("success"));

        if (success2 && responseBody2.containsKey("data")) {
            Map<String, Object> data = (Map<String, Object>) responseBody2.get("data");
            List<Map<String, Object>> content = (List<Map<String, Object>>) data.get("content");

            List<RespCourseDTO> courseList = content.stream().map(item -> {
                RespCourseDTO dto = new RespCourseDTO();
                dto.setId(item.get("id") != null ? Long.parseLong(item.get("id").toString()) : 0L);
                dto.setNama(item.get("nama") != null ? item.get("nama").toString() : "");
                dto.setDeskripsi(item.get("deskripsi") != null ? item.get("deskripsi").toString() : "");
                dto.setJumlahSiswa(item.get("jumlahSiswa") != null ? Long.parseLong(item.get("jumlahSiswa").toString()) : 0L);
                return dto;
            }).toList();

            model.addAttribute("courseList", courseList);
            ValUserCourseProgressForm userCourse = new ValUserCourseProgressForm();
            model.addAttribute("userCourseProgressForm", userCourse);
        }

        return ConstantPage.LIST_USER;
    }


    @GetMapping("/user-list/search")
    public String searchUserByNama(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "nama") String nama,
            HttpServletRequest request,
            Model model
    ) {
        String user = (String) request.getSession().getAttribute("USR_NAME");
        String menuNavbar = (String) request.getSession().getAttribute("MENU_NAVBAR");

        model.addAttribute("USR_NAME", user);
        model.addAttribute("MENU_NAVBAR", menuNavbar);

        // Ambil data user
        ResponseEntity<Object> response = userService.findAllByParam("asc", "id", page, 5, "nama", nama);

        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
        boolean success = Boolean.TRUE.equals(responseBody.get("success"));

        if (success && responseBody.containsKey("data")) {
            Map<String, Object> data = (Map<String, Object>) responseBody.get("data");
            List<Map<String, Object>> content = (List<Map<String, Object>>) data.get("content");

            List<RespUserDTO> userList = content.stream().map(item -> {
                RespUserDTO dto = new RespUserDTO();
                dto.setId(item.get("id") != null ? Long.parseLong(item.get("id").toString()) : 0L);
                dto.setNama(item.get("nama") != null ? item.get("nama").toString() : "");
                dto.setEmail(item.get("email") != null ? item.get("email").toString() : "");
                dto.setUsername(item.get("username") != null ? item.get("username").toString() : "");
                dto.setAlamat(item.get("alamat") != null ? item.get("alamat").toString() : "");
                dto.setTanggalLahir(item.get("tanggalLahir") != null ? LocalDate.parse(item.get("tanggalLahir").toString()) : null);
                dto.setNoHp(item.get("noHp") != null ? item.get("noHp").toString() : "");
                return dto;
            }).toList();

            model.addAttribute("userList", userList);
            model.addAttribute("totalPages", data.get("total-pages"));
            model.addAttribute("currentPage", data.get("current-page"));
        }

        // Ambil data course
        ResponseEntity<Object> response2 = courseService.findAllCourseAsAdmin();
        Map<String, Object> responseBody2 = (Map<String, Object>) response2.getBody();
        boolean success2 = Boolean.TRUE.equals(responseBody2.get("success"));

        if (success2 && responseBody2.containsKey("data")) {
            Map<String, Object> data = (Map<String, Object>) responseBody2.get("data");
            List<Map<String, Object>> content = (List<Map<String, Object>>) data.get("content");

            List<RespCourseDTO> courseList = content.stream().map(item -> {
                RespCourseDTO dto = new RespCourseDTO();
                dto.setId(item.get("id") != null ? Long.parseLong(item.get("id").toString()) : 0L);
                dto.setNama(item.get("nama") != null ? item.get("nama").toString() : "");
                dto.setDeskripsi(item.get("deskripsi") != null ? item.get("deskripsi").toString() : "");
                dto.setJumlahSiswa(item.get("jumlahSiswa") != null ? Long.parseLong(item.get("jumlahSiswa").toString()) : 0L);
                return dto;
            }).toList();

            model.addAttribute("courseList", courseList);
            ValUserCourseProgressForm userCourse = new ValUserCourseProgressForm();
            model.addAttribute("userCourseProgressForm", userCourse);
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

    @GetMapping("/my-course")
    public String goToMyCourseList(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            HttpServletRequest request,
            Model model
    ) {
        String user = (String) request.getSession().getAttribute("USR_NAME");
        String menuNavbar = (String) request.getSession().getAttribute("MENU_NAVBAR");

        model.addAttribute("USR_NAME", user);
        model.addAttribute("MENU_NAVBAR", menuNavbar);

        Long userId = (Long) request.getSession().getAttribute("USR_ID");
        ResponseEntity<Object> response = courseService.findEnrollmentCourse(userId);

        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
        boolean success = (Boolean) responseBody.get("success");

        if (success) {
            List<Map<String, Object>> dataList = (List<Map<String, Object>>) responseBody.get("data");

            List<RespCourseDTO> courseList = dataList.stream().map(item -> {
                RespCourseDTO dto = new RespCourseDTO();
                dto.setId(Long.parseLong(item.get("id").toString()));
                dto.setNama(item.get("nama").toString());
                dto.setDeskripsi(item.get("deskripsi").toString());
                dto.setJumlahSiswa(Long.parseLong(item.get("jumlahSiswa").toString()));
                return dto;
            }).toList();

            model.addAttribute("courseList", courseList);
        }

        // Optional: bisa tetap passing ValCourseDTO jika perlu untuk form
        model.addAttribute("courseDTO", new ValCourseDTO());

        return ConstantPage.MY_COURSE;
    }



    @GetMapping("/detail-course-manage/{courseName}")
    public String goToDetailCourseListById(
            @PathVariable("courseName") String courseName,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam("idCourse") Integer idCourse,
            HttpServletRequest request,
            Model model
    ) {
        String user = (String) request.getSession().getAttribute("USR_NAME");
        String menuNavbar = (String) request.getSession().getAttribute("MENU_NAVBAR");

        model.addAttribute("USR_NAME", user);
        model.addAttribute("MENU_NAVBAR", menuNavbar);
        model.addAttribute("courseName", courseName);

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


            ValDetailCourseForm detailCourse = new ValDetailCourseForm();
            detailCourse.setIdCourse(idCourse);
            model.addAttribute("detailCourseList", detailCourseList);
            model.addAttribute("totalPages", data.get("total-pages"));
            model.addAttribute("currentPage", data.get("current-page"));
            model.addAttribute("detailCourseDTO", detailCourse);
        }

        return ConstantPage.LIST_DETAIL_COURSE;
    }

    @GetMapping("/detail-course/{courseName}")
    public String goToDetailCourseForPeserta(
            @PathVariable("courseName") String courseName,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam("idCourse") Integer idCourse,
            HttpServletRequest request,
            Model model
    ) {
        String user = (String) request.getSession().getAttribute("USR_NAME");
        String menuNavbar = (String) request.getSession().getAttribute("MENU_NAVBAR");

        model.addAttribute("USR_NAME", user);
        model.addAttribute("MENU_NAVBAR", menuNavbar);
        model.addAttribute("courseName", courseName);

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


            ValMapUserDetailCourseDTO mapUserDetailCourseDTO = new ValMapUserDetailCourseDTO();
            model.addAttribute("detailCourseList", detailCourseList);
            model.addAttribute("totalPages", data.get("total-pages"));
            model.addAttribute("currentPage", data.get("current-page"));
            model.addAttribute("mapUserDetailCourseDTO", mapUserDetailCourseDTO);
            model.addAttribute("courseName", courseName);
            model.addAttribute("idCourse", idCourse);
        }
        return ConstantPage.DETAIL_COURSE_PESERTA;
    }

    @GetMapping("/dashboard-admin")
    public String goToDashboardAdmin(
            HttpServletRequest request,
            Model model
    ){
        String user = (String) request.getSession().getAttribute("USR_NAME");
        String menuNavbar = (String) request.getSession().getAttribute("MENU_NAVBAR");

        model.addAttribute("USR_NAME", user);
        model.addAttribute("MENU_NAVBAR", menuNavbar);

        ResponseEntity<Object> response = courseService.findAllMapUserDetailCourseInfo();
        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
        boolean success = (Boolean) responseBody.get("success");

        if (success) {
            Map<String, Object> data = (Map<String, Object>) responseBody.get("data");
            List<Map<String, Object>> contentList = (List<Map<String, Object>>) data.get("content");

            // Ekstrak semua summary
            List<String> summaries = contentList.stream()
                    .map(item -> (String) item.get("summary"))
                    .collect(Collectors.toList());

            // Tambahkan ke model
            model.addAttribute("summaries", summaries);
            model.addAttribute("content", contentList);
            model.addAttribute("data", data); // Diperlukan agar bisa pakai ${data.content} di view
        }

        return ConstantPage.DASHBOARD_ADMIN;
    }

}