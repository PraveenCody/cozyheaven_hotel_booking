package com.springboot.cozyheaven.mapper;

import com.springboot.cozyheaven.dto.request.UserDto;
import com.springboot.cozyheaven.model.User;

public class UserMapper {

    public static User getMappedForUser(UserDto userDto){

        User user = new User();
        user.setPassword(userDto.password());
        user.setUsername(userDto.name());
        return user;

    }

}
