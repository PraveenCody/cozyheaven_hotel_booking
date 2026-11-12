package com.springboot.cozyheaven.controller;

import com.springboot.cozyheaven.dto.request.UserDto;
import com.springboot.cozyheaven.model.User;
import com.springboot.cozyheaven.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/allUsers")
    public List<User> getAll(){
        return userService.getAll();
    }

    @PostMapping("/user{id}")
    public User getUserById(@PathVariable int id){
        return userService.getUserById(id);
    }

    @PutMapping("/user/{id}")
    public void update(@RequestBody UserDto userDto,@PathVariable int id){
        userService.update(userDto,id);
    }

    @DeleteMapping("/user/delete{id}")
    public void deleteById(@PathVariable int id){
        userService.delete(id);
    }

}
