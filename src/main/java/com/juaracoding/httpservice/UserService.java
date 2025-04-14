package com.juaracoding.httpservice;

import com.juaracoding.config.FeignClientConfig;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "user-services", configuration = FeignClientConfig.class, url = "http://localhost:8080/users")
public interface UserService {

    //untuk mengambil semua list peserta
    @GetMapping("/user-list")
    public ResponseEntity<Object> findAllAsAdmin();

    //Mencari data user
    @GetMapping("/{sort}/{sortBy}/{page}")
    public ResponseEntity<Object> findAllByParam(
            @PathVariable(value = "sort") String sort,
            @PathVariable(value = "sortBy") String sortBy,
            @PathVariable(value = "page") Integer page,
            @RequestParam(value = "size") Integer size,
            @RequestParam(value = "column") String column,
            @RequestParam(value = "value") String value) ;
//
//    @GetMapping("/list-peserta/{sort}/{sortBy}/{page}")
//    public ResponseEntity<Object> findPesertaByParam(
//            @PathVariable(value = "sort") String sort,
//            @PathVariable(value = "sortBy") String sortBy,
//            @PathVariable(value = "page") Integer page,
//            @RequestParam(value = "size") Integer size,
//            @RequestParam(value = "column") String column,
//            @RequestParam(value = "value") String value,
//            HttpServletRequest request
//    ) {
//        Pageable pageable = null;
//        sortBy = sortColumnByMap(sortBy);
//        if (sort.equals("asc")) {
//            pageable = PageRequest.of(page, size, Sort.by(sortBy));
//        } else {
//            pageable = PageRequest.of(page, size, Sort.by(sortBy).descending());
//        }
//
//        return userService.findByParam(pageable,column,value,request);
//    }
//
//    //Menghapus data user
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Object> delete(
//            @PathVariable(value = "id") Long id, HttpServletRequest request){
//        return userService.delete(id,request);
//    }
//
//    //Update profile user
//    //akses nya untuk admin dan peserta
//    @PutMapping("/{id}")
//    public ResponseEntity<Object> update(
//            @PathVariable(value = "id") Long id,
//            @Valid @RequestBody ValUserDTO user,
//            HttpServletRequest request){
//        return userService.update(id, userService.convertDtoToEntity(user), request);
//    }
}
