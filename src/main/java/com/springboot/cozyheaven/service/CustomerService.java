package com.springboot.cozyheaven.service;

import com.springboot.cozyheaven.dto.request.CustomerDto;
import com.springboot.cozyheaven.enums.Role;
import com.springboot.cozyheaven.enums.Status;
import com.springboot.cozyheaven.exceptions.OwnershipException;
import com.springboot.cozyheaven.exceptions.ResourceNotFoundException;
import com.springboot.cozyheaven.mapper.CustomerMapper;
import com.springboot.cozyheaven.model.Customer;
import com.springboot.cozyheaven.model.Hotel;
import com.springboot.cozyheaven.model.HotelOwner;
import com.springboot.cozyheaven.model.User;
import com.springboot.cozyheaven.repository.CustomerRepository;
import com.springboot.cozyheaven.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional( rollbackFor = Exception.class)
    public void insert(CustomerDto customerDto) {

        if(customerRepository.getCustomerByUsername(customerDto.username()) != null)
            throw new RuntimeException("User already exist");

        User user = new User();
        user.setUsername(customerDto.username());
        user.setPassword(passwordEncoder.encode(customerDto.password()));
        user.setCreatedAt(Instant.now());
        user.setIsActive(Status.Active);
        user.setRole(Role.CUSTOMER);

        Customer customer = CustomerMapper.getCustomerMapper(customerDto);
        userRepository.save(user);
        customer.setUser(user);
        customerRepository.save(customer);
    }

    public Customer getById(int id) {
        Optional<Customer> opt = customerRepository.findById(id);
        if(opt.isEmpty())
            throw new ResourceNotFoundException("Id not found");

        return opt.get();
    }

    public Customer getCustomer(String name){
        Customer cus = customerRepository.getCustomerByUsername(name);

        if(cus == null)
            throw new ResourceNotFoundException("Not found");

        return cus;
    }


    public List<Customer> getAll() {
        return customerRepository.findAll();
    }

    public void update(int id,String name,String phone,String principalName) {

        Customer customer = getById(id);

        if(!(customer.getUser().getUsername().equals(principalName)))
            throw new OwnershipException("Authorized user can edit");

        if (name != null && !name.isEmpty())
            customer.setName(name);

        if (phone != null && !phone.isEmpty())
            customer.setPhone(phone);

        customerRepository.save(customer);
    }

    public void delete(int id,String name) {

        Customer customer = getById(id);

        if(!customer.getUser().getUsername().equals(name))
            throw new OwnershipException("Authorized user can edit");

        customerRepository.delete(customer);
        userRepository.delete(userRepository.getUserByUserName(name));

    }

    public Customer getCustomerByUsername(String name){
        return customerRepository.getCustomerByUsername(name);
    }

}
