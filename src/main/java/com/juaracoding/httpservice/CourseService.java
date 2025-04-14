package com.juaracoding.httpservice;

import com.juaracoding.config.FeignClientConfig;

import com.juaracoding.dto.validation.ValCourseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(
        name = "course-services",
        configuration = FeignClientConfig.class,
        url = "http://localhost:8080/course"
)
public interface CourseService {

    @GetMapping("/course-list/{page}")
    ResponseEntity<Object> findAllCourse(
            @PathVariable(value = "page") Integer page
    );

    @GetMapping("/{sort}/{sortBy}/{page}")
    ResponseEntity<Object> findByParamsAsAdmin(
            @PathVariable("sort") String sort,
            @PathVariable("sortBy") String sortBy,
            @PathVariable("page") Integer page,
            @RequestParam("size") Integer size,
            @RequestParam("column") String column,
            @RequestParam("value") String value
    );

    @PostMapping
    ResponseEntity<Object> addCourse(
            @RequestBody ValCourseDTO courseDTO
    );

//    @DeleteMapping("/d/{id}")
//    ResponseEntity<Object> deleteCourse(
//            @PathVariable("id") Long id,
//            @RequestHeader("Authorization") String token
//    );
//
//    @PostMapping("/add-detail")
//    ResponseEntity<Object> addDetailCourse(
//            @Valid @RequestBody ValDetailCourseDTO detailCourseDTO,
//            @RequestHeader("Authorization") String token
//    );
//
//    @GetMapping("/detail-course/{page}")
//    ResponseEntity<Object> findDetailCourseByCourse(
//            @PathVariable("page") Integer page,
//            @RequestParam("size") Integer size,
//            @RequestParam("column") String column,
//            @RequestParam("course") String value,
//            @RequestHeader("Authorization") String token
//    );
//
//    @PostMapping("/{idUser}/{idDetailCourse}")
//    ResponseEntity<Object> addProgress(
//            @PathVariable("idUser") long idUser,
//            @PathVariable("idDetailCourse") long idDetailCourse,
//            @RequestBody ValMapUserDetailCourseDTO mapUserDetailCourseDTO,
//            @RequestHeader("Authorization") String token
//    );
//
//    @GetMapping("/all-summary")
//    ResponseEntity<Object> findAllMapUserDetailCourseInfo(
//            @RequestHeader("Authorization") String token
//    );
}
