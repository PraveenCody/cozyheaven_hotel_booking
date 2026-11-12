package com.springboot.cozyheaven;

import com.springboot.cozyheaven.dto.request.HotelOwnerDto;
import com.springboot.cozyheaven.exceptions.OwnershipException;
import com.springboot.cozyheaven.exceptions.ResourceNotFoundException;
import com.springboot.cozyheaven.model.HotelOwner;
import com.springboot.cozyheaven.model.User;
import com.springboot.cozyheaven.repository.HotelOwnerRepository;
import com.springboot.cozyheaven.repository.UserRepository;
import com.springboot.cozyheaven.service.HotelOwnerService;
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
public class HotelOwnerServiceTest {

    @Mock
    private HotelOwnerRepository hotelOwnerRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private HotelOwnerService hotelOwnerService;



    @Test
    public void testInsertException() {

        HotelOwnerDto hotelOwnerDto = new HotelOwnerDto("Gus Fring", "1585258562", "New Mexico","gusfring","gus@123","gus@gmail.com");


        HotelOwner existingOwner = new HotelOwner();

        when(hotelOwnerRepository.getOwnerByUsername(
                hotelOwnerDto.username()
        )).thenReturn(existingOwner);

        assertThrows(RuntimeException.class,
                () -> hotelOwnerService.insert(hotelOwnerDto));

        verify(hotelOwnerRepository, times(1))
                .getOwnerByUsername(hotelOwnerDto.username());

        verify(passwordEncoder, never()).encode(anyString());
        verify(userRepository, never()).save(any(User.class));
        verify(hotelOwnerRepository, never()).save(any(HotelOwner.class));
    }

    @Test
    public void testGetOwnerById() {

        HotelOwner owner1 = new HotelOwner();
        owner1.setName("Gus Fring");

        when(hotelOwnerRepository.findById(1))
                .thenReturn(Optional.of(owner1));

        HotelOwner owner = hotelOwnerService.getOwnerById(1);

        assertEquals(owner1, owner);

        verify(hotelOwnerRepository, times(1)).findById(1);
    }

    @Test
    public void testGetOwnerByIdException() {

        when(hotelOwnerRepository.findById(1))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> hotelOwnerService.getOwnerById(1));

        verify(hotelOwnerRepository, times(1)).findById(1);
    }

    @Test
    public void testUpdateHotelOwner() {

        User user = new User();
        user.setUsername("gus.fring");

        HotelOwner owner = new HotelOwner();
        owner.setName("Gus Fring");
        owner.setPhone("9876543210");
        owner.setAddress("Albuquerque");
        owner.setUser(user);

        when(hotelOwnerRepository.findById(1))
                .thenReturn(Optional.of(owner));

        hotelOwnerService.update(
                1,
                "Walter White",
                "9123456780",
                "Los Pollos Hermanos",
                "gus.fring"
        );

        assertEquals("Walter White", owner.getName());
        assertEquals("9123456780", owner.getPhone());
        assertEquals("Los Pollos Hermanos", owner.getAddress());

        verify(hotelOwnerRepository, times(1)).save(owner);
    }

    @Test
    public void testUpdateHotelOwnerException() {

        User user = new User();
        user.setUsername("gus.fring");

        HotelOwner owner = new HotelOwner();
        owner.setName("Gus Fring");
        owner.setPhone("9876543210");
        owner.setAddress("Albuquerque");
        owner.setUser(user);

        when(hotelOwnerRepository.findById(1))
                .thenReturn(Optional.of(owner));

        assertThrows(OwnershipException.class,
                () -> hotelOwnerService.update(
                        1,
                        "Walter White",
                        "9123456780",
                        "Los Pollos Hermanos",
                        "saul.goodman"
                ));

        verify(hotelOwnerRepository, never())
                .save(any(HotelOwner.class));
    }



    @Test
    public void testGetAll() {

        List<HotelOwner> list = new ArrayList<>();

        HotelOwner owner1 = new HotelOwner();
        owner1.setName("Gus Fring");

        HotelOwner owner2 = new HotelOwner();
        owner2.setName("Saul Goodman");

        HotelOwner owner3 = new HotelOwner();
        owner3.setName("Walter White");

        list.add(owner1);
        list.add(owner2);
        list.add(owner3);

        when(hotelOwnerRepository.findAll()).thenReturn(list);

        List<HotelOwner> owners = hotelOwnerService.getAll();

        assertEquals(3, owners.size());

        verify(hotelOwnerRepository, times(1)).findAll();
    }

}
