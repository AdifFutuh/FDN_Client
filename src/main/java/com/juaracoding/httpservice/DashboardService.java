package com.juaracoding.httpservice;

import com.juaracoding.config.FeignClientConfig;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "dashboard-services",
        configuration = FeignClientConfig.class,
        url = "http://localhost:8080/dashboard"
)
public interface DashboardService {

    @GetMapping("/peserta/{id}")
    public ResponseEntity<Object> dashboardPeserta(
            @PathVariable(value = "id") Long id
    );
}
