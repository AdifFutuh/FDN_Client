package com.juaracoding.httpservice;

import com.juaracoding.config.FeignClientConfig;
import com.juaracoding.dto.validation.ValUserCourseProgressDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        name = "map-user-course-services",
        configuration = FeignClientConfig.class,
        url = "http://localhost:8080/user-course"
)
public interface MapUserCourseService {

    @PostMapping
    void addUserCourse(@RequestBody ValUserCourseProgressDTO userCourseProgressDTO);

    @GetMapping("/progress-approve/{id}")
    public ResponseEntity<Object> approveSummary(
            @PathVariable(value = "id") Long id
    );

    @GetMapping("/progress-reject/{id}")
    public ResponseEntity<Object> rejectSummary(
            @PathVariable(value = "id") Long id
    );
}
