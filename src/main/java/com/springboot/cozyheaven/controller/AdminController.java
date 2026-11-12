package com.springboot.cozyheaven.controller;

import com.springboot.cozyheaven.dto.request.AdminDto;
import com.springboot.cozyheaven.dto.response.AdminRespDto;
import com.springboot.cozyheaven.enums.Status;
import com.springboot.cozyheaven.model.Admin;
import com.springboot.cozyheaven.model.Hotel;
import com.springboot.cozyheaven.service.AdminService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@RestController
@CrossOrigin(origins = "http://localhost:5173/")
public class AdminController {

    private final AdminService adminService;

    @PostMapping("/admin/add/")
    public void insert(@Valid @RequestBody AdminDto adminDto){
        adminService.insert(adminDto);
    }

    @GetMapping("/admin/getAll/")
    public List<Admin> getAll(){
        return adminService.getAll();
    }

    @GetMapping("/admin/")
    public Admin getAdmin(Principal principal){
        return adminService.getAdmin(principal.getName());
    }

    @PutMapping("/admin/update/")
    public void update(@RequestParam(name = "id", required = true) int id,
                       @RequestParam(name = "name",required = false) String name,
                       @RequestParam(name = "phone",required = false) String phone,
                       @RequestParam(name = "email",required = false)String email,
                       @RequestParam(name="status",required = false) Status status){
        adminService.update(id,name,phone,email,status);
    }

    @DeleteMapping("/admin/delete/{id}")
    public void delete(@PathVariable int id){
        adminService.delete(id);
    }

}
