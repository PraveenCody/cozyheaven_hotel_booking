package com.springboot.cozyheaven.service;

import com.springboot.cozyheaven.dto.request.HotelOwnerDto;
import com.springboot.cozyheaven.dto.response.HotelOwnerRespDto;
import com.springboot.cozyheaven.enums.Role;
import com.springboot.cozyheaven.enums.Status;
import com.springboot.cozyheaven.exceptions.OwnershipException;
import com.springboot.cozyheaven.exceptions.ResourceNotFoundException;
import com.springboot.cozyheaven.mapper.HotelOwnerMapper;
import com.springboot.cozyheaven.model.Hotel;
import com.springboot.cozyheaven.model.HotelOwner;
import com.springboot.cozyheaven.model.User;
import com.springboot.cozyheaven.repository.HotelOwnerRepository;
import com.springboot.cozyheaven.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class HotelOwnerService {

    private final UserService userService;
    private final UserRepository userRepository;
    private final HotelOwnerRepository hotelOwnerRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional( rollbackFor = Exception.class)
    public void insert(HotelOwnerDto hotelOwnerDto) {

        if(hotelOwnerRepository.getOwnerByUsername(hotelOwnerDto.username()) != null)
            throw new RuntimeException("User already exist");

        User user=  new User();
        user.setUsername(hotelOwnerDto.username());
        user.setPassword(passwordEncoder.encode(hotelOwnerDto.password()));
        user.setRole(Role.HOTEL_OWNER);
        user.setCreatedAt(Instant.now());
        user.setIsActive(Status.Active);

        HotelOwner hotelOwner = HotelOwnerMapper.getMappedToHotelOWner(hotelOwnerDto);
        userRepository.save(user);
        hotelOwner.setUser(user);
        hotelOwnerRepository.save(hotelOwner);

    }

    public HotelOwner getOwnerById(int id) {

        Optional<HotelOwner> owner = hotelOwnerRepository.findById(id);
        if(owner.isEmpty())
            throw new ResourceNotFoundException("HotelOwner with this id not found...");

        return owner.get();
    }

    public void update(int id, String name, String phone, String address,String principalName) {

        HotelOwner hotelOwner = getOwnerById(id);

        if(!hotelOwner.getUser().getUsername().equals(principalName))
            throw new OwnershipException("Authorized user can edit");

        if (name != null && !name.isEmpty())
            hotelOwner.setName(name);

        if (phone != null && !phone.isEmpty())
            hotelOwner.setPhone(phone);

        if (address != null && !address.isEmpty())
            hotelOwner.setAddress(address);

        hotelOwnerRepository.save(hotelOwner);
    }

    public void delete(int id) {

        HotelOwner hotelOwner = getOwnerById(id);
        userRepository.delete(userService.getUserById(id));
        hotelOwnerRepository.delete(hotelOwner);
    }

    public List<HotelOwner> getAll() {
        return hotelOwnerRepository.findAll();
    }

    public HotelOwner getOwner(String name) {
        return hotelOwnerRepository.getOwnerByUsername(name);
    }
}
