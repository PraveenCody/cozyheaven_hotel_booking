package com.springboot.cozyheaven.service;

import com.springboot.cozyheaven.dto.request.HotelDto;
import com.springboot.cozyheaven.dto.response.*;
import com.springboot.cozyheaven.enums.HotelStatus;
import com.springboot.cozyheaven.exceptions.OwnershipException;
import com.springboot.cozyheaven.exceptions.ResourceNotFoundException;
import com.springboot.cozyheaven.mapper.HotelMapper;
import com.springboot.cozyheaven.model.Hotel;
import com.springboot.cozyheaven.model.HotelOwner;
import com.springboot.cozyheaven.repository.HotelOwnerRepository;
import com.springboot.cozyheaven.repository.HotelRepository;
import com.springboot.cozyheaven.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class HotelService {

    private final HotelRepository hotelRepository;
    private final HotelOwnerRepository hotelOwnerRepository;

    Logger logger = LoggerFactory.getLogger(CustomerService.class);
    private final static String uploadDir = "C:/Users/PRAVEEN/cozyheaven-ui/public/uploads/";


    public void add(HotelDto hotelDto, MultipartFile image, String name) throws IOException {

        HotelOwner hotelOwner = hotelOwnerRepository.getOwnerByUsername(name);

        if (hotelOwner == null)
            throw new ResourceNotFoundException("Hotel Owner not exist");

        String originalFileName = hotelDto.name() + "_" + image.getOriginalFilename();

        Files.copy(image.getInputStream(),Paths.get(uploadDir, originalFileName),StandardCopyOption.REPLACE_EXISTING);

        Hotel hotel = HotelMapper.getMappedForHotel(hotelDto);
        hotel.setStatus(HotelStatus.ACTIVE);
        hotel.setHotelOwner(hotelOwner);
        hotel.setOverAllRating(0.0);
        hotel.setImgUrl(originalFileName);

        hotelRepository.save(hotel);
    }

    public Hotel getById(int id,String name) {

        Optional<Hotel> hotel = hotelRepository.findById(id);

        if (hotel.isEmpty())
            throw new ResourceNotFoundException("Hotel id not found");

        if(!hotel.get().getHotelOwner().getUser().getUsername().equals(name))
            throw new OwnershipException("Cant get this Hotel, need auth");

        return hotel.get();

    }

    public Hotel getHotelById(int id) {

        Optional<Hotel> hotel = hotelRepository.findById(id);

        if (hotel.isEmpty())
            throw new ResourceNotFoundException("Hotel id not found");

        return hotel.get();

    }

    public void update(int id,String name,String description,String location,String city,HotelStatus status,MultipartFile img, String username) throws IOException {

        Hotel hotel = getById(id,username);

        if(!hotel.getHotelOwner().getUser().getUsername().equals(username))
            throw new OwnershipException("You cant Update these hotel,need auth");

        if (name != null && !name.isEmpty())
            hotel.setHotelName(name);

        if (description != null && !description.isEmpty())
            hotel.setDescription(description);

        if (city != null && !city.isEmpty())
            hotel.setCity(city);

        if (location != null && !location.isEmpty())
            hotel.setLocation(location);

        if(status!=null)
            hotel.setStatus(status);

        if (img != null && !img.isEmpty()) {
            Files.deleteIfExists(Paths.get(uploadDir + hotel.getImgUrl()));
            String originalFileName = hotel.getHotelName() + "_" + img.getOriginalFilename();
            Files.copy(img.getInputStream(), Paths.get(uploadDir + originalFileName), StandardCopyOption.REPLACE_EXISTING);
            hotel.setImgUrl(originalFileName);
        }

        hotelRepository.save(hotel);

    }

    public void delete(int id) {

        Hotel hotel = getHotelById(id);

        hotelRepository.delete(hotel);

    }

    public List<Hotel> getAllByOwner(String name) {

        return hotelRepository.findAllByUsername(name);
    }


    public List<Hotel> getAll() {
        return hotelRepository.findAll();
    }

    public ChartDtoHotel chartAdmin() {
        List<HotelStatusDto> list = hotelRepository.chartAdmin();
        return HotelMapper.getMappedForChart(list);
    }

}
