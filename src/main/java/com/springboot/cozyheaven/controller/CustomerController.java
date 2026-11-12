package com.springboot.cozyheaven.controller;

import com.springboot.cozyheaven.dto.request.CustomerDto;
import com.springboot.cozyheaven.dto.request.HotelDto;
import com.springboot.cozyheaven.model.Customer;
import com.springboot.cozyheaven.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RequiredArgsConstructor
@RestController
@CrossOrigin(origins = "http://localhost:5173/")
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping("/customer/add/")
    public void insert(@Valid @RequestBody CustomerDto customerDto){
        customerService.insert(customerDto);
    }

    @GetMapping("/customer/")
    public Customer getCustomer(Principal principal){
        return customerService.getCustomer(principal.getName());
    }

    @GetMapping("/customer/getAll/")
    public List<Customer> getAll(){
        return customerService.getAll();
    }

    @PutMapping("/customer/update/")
    public void update(@RequestParam(name = "id", required = true) int id,
                       @RequestParam(name = "name",required = false) String name,
                       @RequestParam(name = "phone",required = false) String phone, Principal principal){
        customerService.update(id,name,phone,principal.getName());
    }

    @DeleteMapping("/customer/delete/{id}")
    public void delete(@PathVariable int id,Principal principal){
        customerService.delete(id,principal.getName());
    }

}
