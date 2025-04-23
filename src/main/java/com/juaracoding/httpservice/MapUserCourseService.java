package com.juaracoding.httpservice;

import com.juaracoding.config.FeignClientConfig;
import com.juaracoding.dto.validation.ValUserCourseProgressDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        name = "map-user-course-services",
        configuration = FeignClientConfig.class,
        url = "http://localhost:8080/user-course"
)
public interface MapUserCourseService {

    @PostMapping
    ResponseEntity<Object> addUserCourse(
            @RequestBody ValUserCourseProgressDTO userCourseProgressDTO);
}
