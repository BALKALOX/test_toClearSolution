package com.example.test.controller;

import com.example.test.model.User;
import com.example.test.model.dto.UserDto;
import com.example.test.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private final ObjectMapper objectMapper = new ObjectMapper();

    private static final String url = "/api/v1/users";

    private static final UserDto validUserDto = new UserDto(1L,"test@example.com","John","Doe",
            LocalDate.of(2004,4,17),"address","testnum");
    private static final User validUser = new User(1L,"test@example.com","John","Doe",
            LocalDate.of(2004,4,17),"address","testnum");

    private static final UserDto invalidUserDto = new UserDto(1L,"invalid@example.com","John","Doe",
            LocalDate.of(2008,4,17),"address","testnum");


    @Test
    public void testCreateUserWithInvalidUserDtoShouldReturnBadRequest() throws Exception {

        given(userService.createUser(invalidUserDto))
                .willThrow(new IllegalArgumentException());

        mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidUserDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testCreateUserWithInvalidPostBodyShouldReturn() throws Exception {
        mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString("testWrongBody")))
                .andExpect(status().isInternalServerError());
    }
    @Test
    public void testGetAllUsersShouldReturnListOfUsers() throws Exception {
        List<User> userList = new ArrayList<>();
        userList.add(validUser);
        given(userService.getAllUsers()).willReturn(userList);

        mockMvc.perform(get(url))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].email").value(validUser.getEmail()))
                .andExpect(jsonPath("$[0].firstName").value(validUser.getFirstName()));
    }

    @Test
    public void testUpdateUserShouldReturnUpdatedUser() throws Exception {
        given(userService.updateUser(any(Long.class), any(UserDto.class))).willReturn(validUser);

        ResultActions response = mockMvc.perform(put(url+"/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validUserDto))
        );

        response.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.email").value(validUser.getEmail()))
                .andExpect(jsonPath("$.firstName").value(validUser.getFirstName()));
    }
    @Test
    public void testUpdateUserWithInvalidIdShouldReturnNotFound() throws Exception {
        given(userService.updateUser(ArgumentMatchers.eq(999L), any(UserDto.class))).willThrow(new IllegalArgumentException());

        mockMvc.perform(put(url+"/{id}", 999L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validUserDto)))
                .andExpect(status().isBadRequest());
    }
    @Test
    public void testUpdateUserWithInvalidDtoShouldReturnBadRequest() throws Exception {
        given(userService.updateUser(any(Long.class), any(UserDto.class))).willThrow(new IllegalArgumentException());

        mockMvc.perform(put(url+"/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidUserDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testDeleteUserShouldReturnNoContent() throws Exception {
        mockMvc.perform(delete(url+"/{email}", "test@example.com"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testDeleteUserWithEmailNotFoundShouldReturnNotFound() throws Exception {
        doThrow(new IllegalArgumentException("User with email not found"))
                .when(userService).deleteUser("nonexistent@example.com");

        mockMvc.perform(delete(url+"/{email}", "nonexistent@example.com"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testGetUsersByBirthDateRangeShouldReturnListOfUsers() throws Exception {
        List<User> userList = new ArrayList<>();
        userList.add(validUser);
        given(userService.getUsersByBirthDateRange(any(LocalDate.class), any(LocalDate.class))).willReturn(userList);

        mockMvc.perform(get(url + "/birthDate")
                        .param("fromDate", "2024-01-01")
                        .param("toDate", "2024-12-31"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].email").value(validUser.getEmail()))
                .andExpect(jsonPath("$[0].firstName").value(validUser.getFirstName()));
    }
    @Test
    public void testGetUsersByInvalidDateRangeShouldReturnBadRequest() throws Exception {
        given(userService.getUsersByBirthDateRange(any(LocalDate.class), any(LocalDate.class)))
                .willThrow(new IllegalArgumentException("Invalid date range"));

        mockMvc.perform(get(url + "/birthDate")
                        .param("fromDate", "2024-12-31")
                        .param("toDate", "2024-01-01"))
                .andExpect(status().isBadRequest());
    }
}
