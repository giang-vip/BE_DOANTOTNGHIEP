package com.hungnhan.school_management.service;

import com.hungnhan.school_management.dto.request.LoginRequest;
import com.hungnhan.school_management.dto.request.ChangePasswordRequest;
import com.hungnhan.school_management.dto.response.AuthResponse;
import com.hungnhan.school_management.entity.Role;
import com.hungnhan.school_management.entity.User;
import com.hungnhan.school_management.exception.AppException;
import com.hungnhan.school_management.repository.InvalidatedTokenRepository;
import com.hungnhan.school_management.repository.UserRepository;
import com.hungnhan.school_management.security.JwtTokenProvider;
import com.hungnhan.school_management.service.impl.AuthServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.Set;

public class AuthServiceTest {

    @InjectMocks
    private AuthServiceImpl authService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private InvalidatedTokenRepository invalidatedTokenRepository;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private PasswordEncoder passwordEncoder;

    private User user;
    private LoginRequest loginRequest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        Role adminRole = Role.builder().id(1L).name("ADMIN").build();

        user = User.builder()
                .id(1L)
                .username("admin")
                .passwordHash("ENCRYPTED_PASSWORD")
                .email("admin@gmail.com")
                .fullName("Admin User")
                .roles(Set.of(adminRole))
                .build();

        loginRequest = LoginRequest.builder()
                .username("admin")
                .password("123456")
                .build();
    }

    @Test
    void login_happyCase_success() {
        // Given
        Mockito.when(userRepository.findByUsername("admin")).thenReturn(Optional.of(user));
        Mockito.when(passwordEncoder.matches("123456", "ENCRYPTED_PASSWORD")).thenReturn(true);
        Mockito.when(jwtTokenProvider.generateToken(user)).thenReturn("MOCK_JWT_TOKEN");

        // When
        AuthResponse response = authService.login(loginRequest);

        // Then
        Assertions.assertNotNull(response);
        Assertions.assertEquals("MOCK_JWT_TOKEN", response.getToken());
        Assertions.assertEquals("ADMIN", response.getRole());
        Assertions.assertEquals("admin", response.getUserInfo().getUsername());
    }

    @Test
    void login_usernameNotFound_throwException() {
        // Given
        Mockito.when(userRepository.findByUsername("admin")).thenReturn(Optional.empty());

        // When & Then
        Assertions.assertThrows(AppException.class, () -> authService.login(loginRequest));
    }

    @Test
    void login_wrongPassword_throwException() {
        // Given
        Mockito.when(userRepository.findByUsername("admin")).thenReturn(Optional.of(user));
        Mockito.when(passwordEncoder.matches("123456", "ENCRYPTED_PASSWORD")).thenReturn(false);

        // When & Then
        Assertions.assertThrows(AppException.class, () -> authService.login(loginRequest));
    }
}
