package com.example.test.repository;

import com.example.test.model.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Repository class for managing {@link User} entities.
 */
@Repository
@Getter
@Setter
public class UserRepository {

    private final List<User> users = new ArrayList<>();

    /**
     * Retrieves all users.
     *
     * @return List of all users
     */
    public List<User> findAll() {
        return new ArrayList<>(users);
    }

    /**
     * Finds a {@link User} by ID.
     *
     * @param id The ID of the {@link User} to find
     * @return An Optional containing the {@link User} if found, otherwise empty
     */
    public Optional<User> findById(Long id) {
        return users.stream().filter(user -> user.getId().equals(id)).findFirst();
    }

    /**
     * Checks if a {@link User} exists by ID.
     *
     * @param id The ID of the {@link User} to check
     * @return True if the {@link User} exists, otherwise false
     */
    public boolean existsById(Long id) {
        return users.stream().anyMatch(user -> user.getId().equals(id));
    }

    /**
     * Saves a {@link User}.
     *
     * @param user The {@link User} to save
     * @return The saved {@link User}
     */
    public User save(User user) {
        users.add(user);
        return user;
    }

    /**
     * Deletes a {@link User} by email.
     *
     * @param email The email of the {@link User} to delete
     * @return True if the {@link User} was deleted, otherwise false
     */
    public boolean deleteByEmail(String email) {
        return users.removeIf(user -> user.getEmail().equals(email));
    }

    /**
     * Updates a {@link User}.
     *
     * @param id           The ID of the {@link User} to update
     * @param updatedUser  The updated {@link User} data
     * @return The updated user
     * @throws IllegalArgumentException if the {@link User} with the specified ID does not exist
     */
    public User update(Long id, User updatedUser) {
        Optional<User> optionalUser = findById(id);

        if (optionalUser.isPresent()) {
            User existingUser = optionalUser.get();

            if (updatedUser.getEmail() != null) {
                existingUser.setEmail(updatedUser.getEmail());
            }
            if (updatedUser.getFirstName() != null) {
                existingUser.setFirstName(updatedUser.getFirstName());
            }
            if (updatedUser.getLastName() != null) {
                existingUser.setLastName(updatedUser.getLastName());
            }
            if (updatedUser.getBirthDate() != null) {
                existingUser.setBirthDate(updatedUser.getBirthDate());
            }
            if (updatedUser.getAddress() != null) {
                existingUser.setAddress(updatedUser.getAddress());
            }
            if (updatedUser.getPhoneNumber() != null) {
                existingUser.setPhoneNumber(updatedUser.getPhoneNumber());
            }

            return existingUser;
        } else {
            throw new IllegalArgumentException("User not found");
        }
    }
}
