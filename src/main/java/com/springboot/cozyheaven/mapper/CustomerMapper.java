package com.springboot.cozyheaven.mapper;

import com.springboot.cozyheaven.dto.request.CustomerDto;
import com.springboot.cozyheaven.model.Customer;
import org.springframework.stereotype.Component;

@Component
public class CustomerMapper {

    public static Customer getCustomerMapper(CustomerDto customerDto){

        Customer customer = new Customer();
        customer.setName(customerDto.name());
        customer.setPhone(customerDto.phone());
        customer.setEmail(customerDto.email());
        return  customer;

    }

}
