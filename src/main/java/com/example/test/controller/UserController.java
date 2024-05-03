package com.example.test.controller;

import com.example.test.model.User;
import com.example.test.model.dto.UserDto;
import com.example.test.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
/**
 * Controller for managing user-related operations.
 *
 * <p>This controller provides endpoints for retrieving, creating, updating, and deleting users,
 * as well as retrieving users within a specified birth date range.</p>
 *
 * @author [Your Name]
 * @see User
 * @see UserDto
 * @see UserService
 */
@RestController
@RequestMapping("/api/v1/users")
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * Retrieves all users.
     *
     * @return ResponseEntity with a list of all users and {@link HttpStatus} OK
     */
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    /**
     * Creates a new {@link User}.
     *
     * @param userDTO {@link UserDto} object containing {@link User} information
     * @return ResponseEntity with the created {@link User} and HttpStatus CREATED,
     *         or {@link HttpStatus} BAD_REQUEST if user creation fails
     */
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody UserDto userDTO) {

        User createdUser = userService.createUser(userDTO);
        if (createdUser == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(createdUser);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    /**
     * Updates an existing {@link User}.
     *
     * @param id       The ID of the {@link User} to be updated
     * @param userDTO  {@link UserDto} object containing updated {@link User} information
     * @return ResponseEntity with the updated {@link User} and {@link HttpStatus} OK
     */
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody UserDto userDTO) {
        User updatedUser = userService.updateUser(id, userDTO);
        return ResponseEntity.ok(updatedUser);
    }

    /**
     * Partially updates an existing {@link User}.
     *
     * @param id       The ID of the {@link User} to be updated
     * @param userDTO  {@link UserDto} object containing updated {@link User} information
     * @return ResponseEntity with the updated user and {@link HttpStatus} OK
     */
    @PatchMapping("/{id}")
    public ResponseEntity<User> patchUser(@PathVariable Long id, @RequestBody UserDto userDTO) {
        User updatedUser = userService.updateUser(id, userDTO);
        return ResponseEntity.ok(updatedUser);
    }

    /**
     * Deletes a {@link User} by email.
     *
     * @param email The email of the {@link User} to be deleted
     * @return {@link ResponseEntity} with {@link HttpStatus} NO_CONTENT
     */
    @DeleteMapping("/{email}")
    public ResponseEntity<Void> deleteUser(@PathVariable String email) {
        userService.deleteUser(email);
        return ResponseEntity.noContent().build();
    }

    /**
     * Retrieves users within a specified birth date range.
     *
     * @param fromDate The start date of the range (format: "yyyy-MM-dd")
     * @param toDate   The end date of the range (format: "yyyy-MM-dd")
     * @return ResponseEntity with a list of users within the specified birth date range and {@link HttpStatus} OK
     */
    @GetMapping("/birthDate")
    public ResponseEntity<List<User>> getUsersByBirthDateRange(@RequestParam("fromDate") String fromDate,
                                                               @RequestParam("toDate") String toDate) {
        LocalDate from = LocalDate.parse(fromDate);
        LocalDate to = LocalDate.parse(toDate);
        List<User> users = userService.getUsersByBirthDateRange(from, to);
        return ResponseEntity.ok(users);
    }

    /**
     * Handles {@link  IllegalArgumentException} thrown by controller methods.
     *
     * @param ex The {@link  IllegalArgumentException}
     * @return {@link  ResponseEntity} with the exception message and {@link HttpStatus} BAD_REQUEST
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    /**
     * Handles other exceptions thrown by controller methods.
     *
     * @param ex The {@link  Exception}
     * @return {@link  ResponseEntity} with a generic error message and {@link HttpStatus} INTERNAL_SERVER_ERROR
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something went wrong");
    }
}