package com.example.test.service;

import com.example.test.model.User;
import com.example.test.model.dto.UserDto;

import java.time.LocalDate;
import java.util.List;

/**
 * Service interface for managing users.
 *
 * <p>
 * This interface provides methods for retrieving, creating, updating, and deleting users,
 * as well as retrieving users within a specified birth date range.
 * </p>
 *
 * @author Anton Misiura
 * @see User
 * @see UserDto
 */
public interface UserService {

    /**
     * Retrieves all users.
     *
     * @return List of users
     */
    List<User> getAllUsers();

    /**
     * Creates a new {@link User}.
     *
     * @param userDTO {@link UserDto} object containing user information
     * @return The created {@link User}
     * @throws IllegalArgumentException if user is too young
     */
    User createUser(UserDto userDTO);

    /**
     * Updates an existing {@link User}.
     *
     * @param id       The ID of the {@link User} to be updated
     * @param userDTO  {@link UserDto} object containing updated user information
     * @return The updated user
     * @throws IllegalArgumentException if {@link User} with specified ID does not exist
     */
    User updateUser(Long id, UserDto userDTO);

    /**
     * Deletes a {@link User} by email.
     *
     * @param email The email of the {@link User} to be deleted
     * @throws IllegalArgumentException if {@link User} with specified email does not exist
     */
    void deleteUser(String email);

    /**
     * Retrieves users within a specified birth date range.
     *
     * @param fromDate The start date of the range (type {@link LocalDate})
     * @param toDate   The end date of the range (type {@link LocalDate})
     * @return List of users within the specified birth date range
     * @throws IllegalArgumentException if fromDate is after toDate or if fromDate is equal to toDate
     */
    List<User> getUsersByBirthDateRange(LocalDate fromDate, LocalDate toDate);
}
