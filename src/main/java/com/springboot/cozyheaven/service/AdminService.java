package com.springboot.cozyheaven.service;

import com.springboot.cozyheaven.dto.request.AdminDto;
import com.springboot.cozyheaven.dto.response.AdminRespDto;
import com.springboot.cozyheaven.enums.Role;
import com.springboot.cozyheaven.enums.Status;
import com.springboot.cozyheaven.exceptions.OwnershipException;
import com.springboot.cozyheaven.exceptions.ResourceNotFoundException;
import com.springboot.cozyheaven.mapper.AdminMapper;
import com.springboot.cozyheaven.model.Admin;
import com.springboot.cozyheaven.model.User;
import com.springboot.cozyheaven.repository.AdminRepository;
import com.springboot.cozyheaven.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AdminService {

    private final AdminRepository adminRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional( rollbackFor = Exception.class)
    public void insert(AdminDto adminDto) {

        if(userRepository.getUserByUserName(adminDto.username()) != null)
            throw new RuntimeException("User already exist");

        User user = new User();
        user.setUsername(adminDto.username());
        user.setPassword(passwordEncoder.encode(adminDto.password()));
        user.setRole(Role.ADMIN);
        user.setCreatedAt(Instant.now());
        user.setIsActive(Status.Active);

        Admin admin = AdminMapper.getMappedForAdmin(adminDto);
        admin.setUser(user);
        userRepository.save(user);
        adminRepository.save(admin);

    }

    public Admin getById(int id) {

        Optional<Admin> admin = adminRepository.findById(id);

        if(admin.isEmpty())
            throw new ResourceNotFoundException("Hotel id not found");

        return admin.get();

    }

    public void update(int id,String name,String phone,String email,Status status) {

        Admin admin = getById(id);

        if (name != null && ! name.isEmpty())
            admin.setName(name);

        if (phone != null && !phone.isEmpty())
            admin.setPhone(phone);

        if(email != null && !email.isEmpty())
            admin.setEmail(email);

        if(status != null)
            admin.getUser().setIsActive(status);

        adminRepository.save(admin);

    }

    public void delete(int id) {

        Admin admin = getById(id);
        userRepository.deleteById(admin.getUser().getId());
        adminRepository.delete(admin);

    }

    public List<Admin> getAll() {
        return adminRepository.findAll();
    }

    public Admin getAdmin(String name) {

        Admin admin = adminRepository.getAdminByUsername(name);

        if(admin == null)
            throw new ResourceNotFoundException("Admin not found!");

        return admin;
    }
}
