package com.springboot.cozyheaven.controller;

import com.springboot.cozyheaven.dto.response.DetailsDto;
import com.springboot.cozyheaven.dto.response.TokenDto;
import com.springboot.cozyheaven.service.UserService;
import com.springboot.cozyheaven.utility.JwtUtility;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173/")
public class AuthController {

    private final JwtUtility jwtUtility;
    private final UserService userService;

    /* This api is authenticated by Spring. which means if the control reaches this method, Spring
     * already has checked the credentials and it can share the username */

    @GetMapping("/auth/login")
    public TokenDto login(Principal principal){

        String loggedInUsername = principal.getName();
        String token = jwtUtility.generateToken(loggedInUsername);
        return new TokenDto(token,
                jwtUtility.extractExpiration(token),
                LocalDateTime.now().toString());

    }

    @GetMapping("/auth/user-details")
    public DetailsDto getUserDetails(Principal principal){
        return userService.getUserDetails(principal.getName());
    }

}
