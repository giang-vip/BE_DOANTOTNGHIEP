package com.hungnhan.school_management.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hungnhan.school_management.dto.request.UserCreationRequest;
import com.hungnhan.school_management.dto.request.UserUpdateRequest;
import com.hungnhan.school_management.dto.response.PageResponse;
import com.hungnhan.school_management.dto.response.UserResponse;
import com.hungnhan.school_management.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void createUser_ValidRequest_ReturnsOk() throws Exception {
        UserCreationRequest request = new UserCreationRequest();
        request.setUsername("newuser");
        request.setPassword("123456");
        request.setEmail("new@gmail.com");
        request.setFullName("Nguyễn Văn A");
        request.setRoles(Set.of("ADMIN"));

        UserResponse mockResponse = new UserResponse();
        mockResponse.setUsername("newuser");

        Mockito.when(userService.createUser(any(UserCreationRequest.class))).thenReturn(mockResponse);

        mockMvc.perform(post("/api/admin/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(1000))
                .andExpect(jsonPath("$.result.username").value("newuser"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void createUser_InvalidRequest_ReturnsBadRequest() throws Exception {
        UserCreationRequest request = new UserCreationRequest(); // missing required fields

        mockMvc.perform(post("/api/admin/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "teacher", roles = {"TEACHER"})
    void createUser_NotAdmin_ReturnsForbidden() throws Exception {
        UserCreationRequest request = new UserCreationRequest();
        request.setUsername("newuser");
        request.setPassword("123456");

        mockMvc.perform(post("/api/admin/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void getUserById_ValidId_ReturnsOk() throws Exception {
        UserResponse mockResponse = new UserResponse();
        mockResponse.setId(1L);
        mockResponse.setUsername("testuser");

        Mockito.when(userService.getUserById(1L)).thenReturn(mockResponse);

        mockMvc.perform(get("/api/admin/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(1000))
                .andExpect(jsonPath("$.result.id").value(1));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void updateUser_ValidRequest_ReturnsOk() throws Exception {
        UserUpdateRequest request = new UserUpdateRequest();
        request.setEmail("updated@gmail.com");

        UserResponse mockResponse = new UserResponse();
        mockResponse.setEmail("updated@gmail.com");

        Mockito.when(userService.updateUser(eq(1L), any(UserUpdateRequest.class))).thenReturn(mockResponse);

        mockMvc.perform(put("/api/admin/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(1000))
                .andExpect(jsonPath("$.result.email").value("updated@gmail.com"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void deleteUser_ValidId_ReturnsOk() throws Exception {
        Mockito.doNothing().when(userService).deleteUser(1L);

        mockMvc.perform(delete("/api/admin/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(1000));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void getAllUsers_ReturnsOk() throws Exception {
        PageResponse<UserResponse> mockPage = new PageResponse<>();
        mockPage.setContent(List.of(new UserResponse()));
        mockPage.setTotalElements(1);

        Mockito.when(userService.getUsers(any(), any(), eq(0), eq(10))).thenReturn(mockPage);

        mockMvc.perform(get("/api/admin/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(1000))
                .andExpect(jsonPath("$.result.totalElements").value(1));
    }
}
