package com.springboot.cozyheaven;

import com.springboot.cozyheaven.dto.request.HotelDto;
import com.springboot.cozyheaven.enums.HotelStatus;
import com.springboot.cozyheaven.exceptions.OwnershipException;
import com.springboot.cozyheaven.exceptions.ResourceNotFoundException;
import com.springboot.cozyheaven.mapper.HotelMapper;
import com.springboot.cozyheaven.model.Hotel;
import com.springboot.cozyheaven.model.HotelOwner;
import com.springboot.cozyheaven.model.User;
import com.springboot.cozyheaven.repository.HotelOwnerRepository;
import com.springboot.cozyheaven.repository.HotelRepository;
import com.springboot.cozyheaven.service.HotelService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HotelServiceTest {

    @Mock
    private HotelRepository hotelRepository;

    @Mock
    private HotelOwnerRepository hotelOwnerRepository;

    @InjectMocks
    private HotelService hotelService;



    @Test
    public void testGetById() {

        User user = new User();
        user.setUsername("gus fring");

        HotelOwner owner = new HotelOwner();
        owner.setUser(user);

        Hotel hotel = new Hotel();
        hotel.setHotelOwner(owner);
        hotel.setHotelName("Los Pollos Hermanos");

        when(hotelRepository.findById(1))
                .thenReturn(Optional.of(hotel));

        Hotel result = hotelService.getById(1, "gus fring");

        assertEquals(hotel, result);

        verify(hotelRepository).findById(1);
    }

    @Test
    public void testGetByIdNotFound() {

        when(hotelRepository.findById(1))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> hotelService.getById(1, "gus fring"));

        verify(hotelRepository).findById(1);
    }

    @Test
    public void testGetByIdException() {

        User user = new User();
        user.setUsername("gus fring");

        HotelOwner owner = new HotelOwner();
        owner.setUser(user);

        Hotel hotel = new Hotel();
        hotel.setHotelOwner(owner);

        when(hotelRepository.findById(1))
                .thenReturn(Optional.of(hotel));

        assertThrows(OwnershipException.class,
                () -> hotelService.getById(1, "saul goodman"));

        verify(hotelRepository).findById(1);
    }

    @Test
    public void testGetHotelById() {

        Hotel hotel = new Hotel();
        hotel.setHotelName("Los Pollos Hermanos");

        when(hotelRepository.findById(1))
                .thenReturn(Optional.of(hotel));

        Hotel result = hotelService.getHotelById(1);

        assertEquals(hotel, result);

        verify(hotelRepository).findById(1);
    }

    @Test
    public void testGetHotelByIdException() {

        when(hotelRepository.findById(1))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> hotelService.getHotelById(1));

        verify(hotelRepository).findById(1);
    }



    @Test
    public void testDelete() {

        User user = new User();
        user.setUsername("gus fring");

        HotelOwner owner = new HotelOwner();
        owner.setUser(user);

        Hotel hotel = new Hotel();
        hotel.setHotelOwner(owner);

        when(hotelRepository.findById(1))
                .thenReturn(Optional.of(hotel));

        hotelService.delete(1);

        verify(hotelRepository).findById(1);
        verify(hotelRepository).delete(hotel);
    }

    @Test
    public void testDeleteException() {

        when(hotelRepository.findById(1))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> hotelService.delete(1));

        verify(hotelRepository, never()).delete(any(Hotel.class));
    }

    @Test
    public void testGetAllByOwner() {

        List<Hotel> list = new ArrayList<>();

        Hotel hotel1 = new Hotel();
        hotel1.setHotelName("Los Pollos Hermanos");

        Hotel hotel2 = new Hotel();
        hotel2.setHotelName("A1A Car Wash");

        list.add(hotel1);
        list.add(hotel2);

        when(hotelRepository.findAllByUsername("gus fring"))
                .thenReturn(list);

        List<Hotel> result = hotelService.getAllByOwner("gus fring");

        assertEquals(list, result);
        assertEquals(2, result.size());

        verify(hotelRepository).findAllByUsername("gus fring");
    }

    @Test
    public void testGetAllByOwnerEmpty() {

        when(hotelRepository.findAllByUsername("gus fring"))
                .thenReturn(new ArrayList<>());

        List<Hotel> result = hotelService.getAllByOwner("gus fring");

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(hotelRepository).findAllByUsername("gus fring");
    }
}