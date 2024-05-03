package com.example.test.mapper;

import com.example.test.model.User;
import com.example.test.model.dto.UserDto;
import org.springframework.stereotype.Component;

/**
 * Mapper class for converting between {@link User} and {@link UserDto}.
 */
@Component
public class UserMapper {

    /**
     * Converts a {@link UserDto} object to a {@link User} object.
     *
     * @param userDTO The {@link UserDto} object to convert
     * @return The converted {@link User} object
     */
    public User toEntity(UserDto userDTO) {
        User user = new User();
        user.setId(userDTO.getId());
        user.setEmail(userDTO.getEmail());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setBirthDate(userDTO.getBirthDate());
        user.setAddress(userDTO.getAddress());
        user.setPhoneNumber(userDTO.getPhoneNumber());
        return user;
    }

    /**
     * Converts a {@link User} object to a {@link UserDto} object.
     *
     * @param user The {@link User} object to convert
     * @return The converted {@link UserDto} object
     */
    public UserDto toDTO(User user) {
        UserDto userDTO = new UserDto();
        userDTO.setId(user.getId());
        userDTO.setEmail(user.getEmail());
        userDTO.setFirstName(user.getFirstName());
        userDTO.setLastName(user.getLastName());
        userDTO.setBirthDate(user.getBirthDate());
        userDTO.setAddress(user.getAddress());
        userDTO.setPhoneNumber(user.getPhoneNumber());
        return userDTO;
    }
}
