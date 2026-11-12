package com.springboot.cozyheaven.mapper;

import com.springboot.cozyheaven.dto.request.AdminDto;
import com.springboot.cozyheaven.model.Admin;

public class AdminMapper {

    public static Admin getMappedForAdmin(AdminDto adminDto){

        Admin admin = new Admin();
        admin.setName(adminDto.name());
        admin.setPhone(adminDto.phone());
        admin.setEmail(adminDto.email());
        return admin;

    }

}

