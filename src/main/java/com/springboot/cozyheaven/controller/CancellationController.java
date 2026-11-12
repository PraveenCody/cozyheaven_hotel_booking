package com.springboot.cozyheaven.controller;

import com.springboot.cozyheaven.dto.request.CancellationDto;
import com.springboot.cozyheaven.model.Cancellation;
import com.springboot.cozyheaven.service.CancellationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RequiredArgsConstructor
@RestController
@CrossOrigin(origins = "http://localhost:5173/")
public class CancellationController {

    private final CancellationService cancellationService;

    @PostMapping("/cancellation/{bid}")
    public void cancel(@Valid @RequestBody CancellationDto cancellationDto, @PathVariable int bid, Principal principal){
        cancellationService.cancel(cancellationDto,bid,principal.getName());
    }

    @GetMapping("/cancellation/all/")
    public List<Cancellation> allOfCustomer(Principal principal){
        return cancellationService.allOfCustomer(principal.getName());
    }

}
