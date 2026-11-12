package com.springboot.cozyheaven;

import com.springboot.cozyheaven.dto.request.CustomerDto;
import com.springboot.cozyheaven.exceptions.OwnershipException;
import com.springboot.cozyheaven.exceptions.ResourceNotFoundException;
import com.springboot.cozyheaven.model.Customer;
import com.springboot.cozyheaven.model.User;
import com.springboot.cozyheaven.repository.CustomerRepository;
import com.springboot.cozyheaven.repository.UserRepository;
import com.springboot.cozyheaven.service.CustomerService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceTest {

    @InjectMocks
    private CustomerService customerService;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    public void testInsert() {

        Customer customer = new Customer();
        User user = new User();

        CustomerDto customerDto = new CustomerDto(
                "name", "1585258562", "names", "name@123", "name@gmail.com"
        );

        when(customerRepository.getCustomerByUsername(customerDto.username())).thenReturn(null);

        when(passwordEncoder.encode("name@123")).thenReturn("encodedPassword");

        when(userRepository.save(any(User.class))).thenReturn(user);

        when(customerRepository.save(any(Customer.class))).thenReturn(customer);

        customerService.insert(customerDto);

        verify(customerRepository, times(1)).getCustomerByUsername(customerDto.username());

        verify(passwordEncoder,times(1)).encode("name@123");

        verify(userRepository, times(1)).save(any(User.class));

        verify(customerRepository, times(1)).save(any(Customer.class));
    }

    @Test
    public void testGetById(){

        Customer customer1 = new Customer();

        when(customerRepository.findById(1)).thenReturn(Optional.of(customer1));

        Customer customer = customerService.getById(1);

        assertEquals(customer1,customer);

        verify(customerRepository,times(1)).findById(1);

    }

    @Test
    public void testGetAll(){

        List<Customer> list = new ArrayList<>();
        when(customerRepository.findAll()).thenReturn(list);
        List<Customer> lists = customerService.getAll();
        assertEquals(lists,list);
        verify(customerRepository,times(1)).findAll();

    }

    @Test
    void updateCustomerTest() {

        User user = new User();
        user.setUsername("name");

        Customer customer = new Customer();
        customer.setName("name");
        customer.setPhone("9876543210");
        customer.setUser(user);

        when(customerRepository.findById(1)).thenReturn(Optional.of(customer));

        customerService.update(1, "New Name", "9123456780", "name");

        assertEquals("New Name", customer.getName());
        assertEquals("9123456780", customer.getPhone());

        verify(customerRepository, times(1)).save(customer);
    }

    @Test
    void updateException(){
        User user = new User();
        user.setUsername("name");
        Customer customer = new Customer();
        customer.setName("name");
        customer.setPhone("9876543210");
        customer.setUser(user);

        when(customerRepository.findById(1)).thenReturn(Optional.of(customer));

        assertThrows(OwnershipException.class,
                () -> customerService.update(1, "New Name", "9123456780", "anotherUser")
        );

        verify(customerRepository, never()).save(any(Customer.class));
    }

    @Test
    void deleteCustomerTest() {

        User user = new User();
        user.setUsername("name");
        Customer customer = new Customer();
        customer.setName("name");
        customer.setPhone("9876543210");
        customer.setUser(user);

        when(customerRepository.findById(1)).thenReturn(Optional.of(customer));

        when(userRepository.getUserByUserName("name")).thenReturn(user);

        customerService.delete(1, "name");

        verify(customerRepository, times(1)).delete(customer);
        verify(userRepository, times(1)).getUserByUserName("name");
        verify(userRepository, times(1)).delete(user);
    }

    @Test
    void getCustomerByUsernameTest() {

        User user = new User();
        user.setUsername("name");
        Customer customer = new Customer();
        customer.setName("name");
        customer.setPhone("9876543210");
        customer.setUser(user);

        when(customerRepository.getCustomerByUsername("name")).thenReturn(customer);

        Customer result = customerService.getCustomerByUsername("name");

        assertEquals("name", result.getName());
        assertEquals("name", result.getUser().getUsername());

        verify(customerRepository, times(1)).getCustomerByUsername("name");
    }

}
