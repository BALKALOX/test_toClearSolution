package com.example.test.service.impl;

import com.example.test.mapper.UserMapper;
import com.example.test.model.User;
import com.example.test.model.dto.UserDto;
import com.example.test.repository.UserRepository;
import com.example.test.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Implementation of the {@link UserService} interface.
 *
 * @author Anton Misiura
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final UserMapper userMapper;

    private final int userMinAge;

    /**
     * Constructs a new UserServiceImpl with the specified dependencies.
     *
     * @param userRepository The UserRepository implementation
     * @param userMapper     The UserMapper implementation
     * @param env            The Environment object
     */
    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper, Environment env) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.userMinAge = Integer.parseInt(env.getProperty("user.min.age"));
    }

    /**
     * Retrieves all users.
     *
     * @return List of all users
     */
    @Override
    public List<User> getAllUsers() {
        return userRepository.getUsers();
    }

    /**
     * Creates a new {@link User}.
     *
     * @param userDTO The user data to create the new user
     * @return The newly created user
     * @throws IllegalArgumentException if the user is too young
     */
    @Override
    public User createUser(UserDto userDTO) {

        User user = userMapper.toEntity(userDTO);

        var birthDate = user.getBirthDate();
        var currentDate = LocalDate.now();
        var ageDifference = Period.between(birthDate, currentDate);

        if (ageDifference.getYears() < userMinAge) {
            throw new IllegalArgumentException("User must be at least " + userMinAge + " years old");
        }
        userRepository.save(user);
        return user;
    }

    /**
     * Updates an existing {@link User}.
     *
     * @param id       The ID of the {@link User} to be updated
     * @param userDTO  The updated {@link User} data ({@link UserDto})
     * @return The updated {@link User}
     * @throws IllegalArgumentException if the {@link User} with the specified ID does not exist
     */
    @Override
    public User updateUser(Long id, UserDto userDTO) {

        Objects.requireNonNull(userDTO, "UserDto must not be null");
        var exists = userRepository.existsById(id);
        if (!exists)  {
            throw  new IllegalArgumentException("User not found");
        }

        User updatedUser = userMapper.toEntity(userDTO);
        return userRepository.update(id,updatedUser);
    }
    /**
     * Deletes a {@link User} by email.
     *
     * @param email The email of the {@link User} to be deleted
     * @throws IllegalArgumentException if the {@link User} with the specified email does not exist
     */
    @Override
    public void deleteUser(String email) {
        var isDeleted = userRepository.deleteByEmail(email);
        if (!isDeleted){
            throw new IllegalArgumentException("User with email: " + email + " doesn't exists");
        }
    }
    /**
     * Retrieves users within a specified birth date range.
     *
     * @param fromDate The start date of the range (type {@link LocalDate})
     * @param toDate   The end date of the range (type {@link LocalDate})
     * @return List of users within the specified birth date range
     * @throws IllegalArgumentException if the fromDate is after toDate or if they are equal
     */
    @Override
    public List<User> getUsersByBirthDateRange(LocalDate fromDate, LocalDate toDate) {
        if (fromDate.isAfter(toDate) || fromDate.equals(toDate)){
            throw new IllegalArgumentException("Wrong date range");
        }
        List<User> users = userRepository.getUsers();
        List<User> usersInRange = new ArrayList<>();
        for (User user : users) {
            if (user.getBirthDate().isAfter(fromDate) && user.getBirthDate().isBefore(toDate)) {
                usersInRange.add(user);
                System.out.println(user.getBirthDate());
            }
        }
        return usersInRange;
    }
}
