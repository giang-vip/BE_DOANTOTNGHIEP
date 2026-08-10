package com.hungnhan.school_management.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hungnhan.school_management.dto.request.ChangePasswordRequest;
import com.hungnhan.school_management.dto.request.LoginRequest;
import com.hungnhan.school_management.dto.response.AuthResponse;
import com.hungnhan.school_management.dto.response.UserResponse;
import com.hungnhan.school_management.service.AuthService;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthService authService;

    @Test
    void login_ValidRequest_ReturnsOk() throws Exception {
        LoginRequest request = new LoginRequest("admin", "123456");
        AuthResponse mockResponse = new AuthResponse();
        mockResponse.setToken("mock_token");
        mockResponse.setRole("ADMIN");
        
        AuthResponse.UserInfo userResponse = new AuthResponse.UserInfo();
        userResponse.setUsername("admin");
        mockResponse.setUserInfo(userResponse);

        Mockito.when(authService.login(any(LoginRequest.class))).thenReturn(mockResponse);

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(1000))
                .andExpect(jsonPath("$.result.token").value("mock_token"))
                .andExpect(jsonPath("$.result.role").value("ADMIN"))
                .andExpect(jsonPath("$.result.userInfo.username").value("admin"));
    }

    @Test
    void login_InvalidRequest_ReturnsBadRequest() throws Exception {
        // Missing username and password
        LoginRequest request = new LoginRequest("", "");

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").exists()); // Code is defined by ExceptionHandler
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void logout_ValidRequest_ReturnsOk() throws Exception {
        Mockito.doNothing().when(authService).logout(anyString());

        mockMvc.perform(post("/api/auth/logout")
                .header("Authorization", "Bearer valid_token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(1000))
                .andExpect(jsonPath("$.result.success").value(true));
    }

    @Test
    void logout_WithoutToken_ReturnsUnauthorized() throws Exception {
        mockMvc.perform(post("/api/auth/logout"))
                .andExpect(status().isUnauthorized()); // Missing request header 'Authorization' or not authenticated
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void getMe_ValidRequest_ReturnsOk() throws Exception {
        UserResponse mockResponse = new UserResponse();
        mockResponse.setUsername("admin");
        mockResponse.setFullName("Admin User");

        Mockito.when(authService.getMe()).thenReturn(mockResponse);

        mockMvc.perform(get("/api/auth/me"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(1000))
                .andExpect(jsonPath("$.result.username").value("admin"))
                .andExpect(jsonPath("$.result.fullName").value("Admin User"));
    }

    @Test
    void getMe_Unauthenticated_ReturnsUnauthorized() throws Exception {
        mockMvc.perform(get("/api/auth/me"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void changePassword_ValidRequest_ReturnsOk() throws Exception {
        ChangePasswordRequest request = new ChangePasswordRequest("oldPass", "newPass");

        Mockito.doNothing().when(authService).changePassword(any(ChangePasswordRequest.class));

        mockMvc.perform(put("/api/auth/change-password")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(1000))
                .andExpect(jsonPath("$.result.success").value(true));
    }
}
