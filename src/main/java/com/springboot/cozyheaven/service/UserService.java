package com.springboot.cozyheaven.service;

import com.springboot.cozyheaven.dto.request.UserDto;
import com.springboot.cozyheaven.dto.response.DetailsDto;
import com.springboot.cozyheaven.enums.Role;
import com.springboot.cozyheaven.exceptions.ResourceNotFoundException;
import com.springboot.cozyheaven.mapper.UserMapper;
import com.springboot.cozyheaven.model.User;
import com.springboot.cozyheaven.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public User getUserById(int id) {
        Optional<User> opt =  userRepository.findById(id);

        if(opt.isEmpty())
            throw new ResourceNotFoundException("Id not found");

        return opt.get();
    }

    public void delete(int id) {
        getUserById(id);
        userRepository.deleteById(id);
    }

    public void update(UserDto userDto,int id) {

        User user = getUserById(id);

        if(userDto.name() != null || !userDto.name().isEmpty())
            user.setUsername(userDto.name());

        if(userDto.password()!= null || !userDto.password().isEmpty())
            user.setPassword(userDto.password());

    }

    public DetailsDto getUserDetails(String name) {
        return userRepository.getUserForLogin(name);
    }
}
