package com.hungnhan.school_management.service.impl;

import com.hungnhan.school_management.dto.request.LoginRequest;
import com.hungnhan.school_management.dto.response.AuthResponse;
import com.hungnhan.school_management.entity.Role;
import com.hungnhan.school_management.entity.User;
import com.hungnhan.school_management.exception.AppException;
import com.hungnhan.school_management.exception.ErrorCode;
import com.hungnhan.school_management.repository.UserRepository;
import com.hungnhan.school_management.security.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;
import io.jsonwebtoken.Claims;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import com.hungnhan.school_management.mapper.UserMapper;
import com.hungnhan.school_management.repository.InvalidatedTokenRepository;
import com.hungnhan.school_management.dto.request.ChangePasswordRequest;
import com.hungnhan.school_management.dto.response.UserResponse;
import com.hungnhan.school_management.entity.InvalidatedToken;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private InvalidatedTokenRepository invalidatedTokenRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private AuthServiceImpl authService;

    private User mockUser;
    private Role mockRole;

    @BeforeEach
    void setUp() {
        mockRole = new Role();
        mockRole.setName("ADMIN");

        mockUser = new User();
        mockUser.setId(1L);
        mockUser.setUsername("admin");
        mockUser.setPasswordHash("encoded_password");
        mockUser.setRoles(java.util.Set.of(mockRole));
        mockUser.setStatus(com.hungnhan.school_management.constant.UserStatus.ACTIVE);
    }

    @Test
    void login_Success() {
        // Arrange
        LoginRequest request = new LoginRequest("admin", "password123");
        
        when(userRepository.findByUsername(request.getUsername())).thenReturn(Optional.of(mockUser));
        when(passwordEncoder.matches(request.getPassword(), mockUser.getPasswordHash())).thenReturn(true);
        when(jwtTokenProvider.generateToken(mockUser)).thenReturn("mocked_jwt_token");

        // Act
        AuthResponse response = authService.login(request);

        // Assert
        assertNotNull(response);
        assertEquals("mocked_jwt_token", response.getToken());
        assertEquals("ADMIN", response.getRole());
        assertEquals("admin", response.getUserInfo().getUsername());
        
        verify(userRepository, times(1)).findByUsername(request.getUsername());
        verify(passwordEncoder, times(1)).matches(request.getPassword(), mockUser.getPasswordHash());
        verify(jwtTokenProvider, times(1)).generateToken(mockUser);
    }

    @Test
    void login_UserNotFound_ThrowsException() {
        // Arrange
        LoginRequest request = new LoginRequest("invalid_user", "password123");
        
        when(userRepository.findByUsername(request.getUsername())).thenReturn(Optional.empty());

        // Act & Assert
        AppException exception = assertThrows(AppException.class, () -> authService.login(request));
        assertEquals(ErrorCode.UNAUTHENTICATED, exception.getErrorCode());
        
        verify(passwordEncoder, never()).matches(anyString(), anyString());
        verify(jwtTokenProvider, never()).generateToken(any());
    }

    @Test
    void login_WrongPassword_ThrowsException() {
        // Arrange
        LoginRequest request = new LoginRequest("admin", "wrong_password");
        
        when(userRepository.findByUsername(request.getUsername())).thenReturn(Optional.of(mockUser));
        when(passwordEncoder.matches(request.getPassword(), mockUser.getPasswordHash())).thenReturn(false);

        // Act & Assert
        AppException exception = assertThrows(AppException.class, () -> authService.login(request));
        assertEquals(ErrorCode.UNAUTHENTICATED, exception.getErrorCode());
        
        verify(jwtTokenProvider, never()).generateToken(any());
    }

    @Test
    void logout_Success() {
        // Arrange
        String token = "Bearer valid_token";
        Claims mockClaims = mock(Claims.class);
        when(mockClaims.getId()).thenReturn(UUID.randomUUID().toString());
        when(mockClaims.getExpiration()).thenReturn(new java.util.Date());
        when(jwtTokenProvider.getClaimsFromJWT("valid_token")).thenReturn(mockClaims);

        // Act
        assertDoesNotThrow(() -> authService.logout(token));

        // Assert
        verify(invalidatedTokenRepository, times(1)).save(any(InvalidatedToken.class));
    }

    @Test
    void getMe_Success() {
        // Arrange
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getName()).thenReturn("admin");
        when(userRepository.findByUsername("admin")).thenReturn(Optional.of(mockUser));
        
        UserResponse mockResponse = new UserResponse();
        mockResponse.setUsername("admin");
        when(userMapper.toUserResponse(mockUser)).thenReturn(mockResponse);

        // Act
        UserResponse response = authService.getMe();

        // Assert
        assertNotNull(response);
        assertEquals("admin", response.getUsername());
    }

    @Test
    void changePassword_Success() {
        // Arrange
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getName()).thenReturn("admin");
        when(userRepository.findByUsername("admin")).thenReturn(Optional.of(mockUser));

        ChangePasswordRequest request = new ChangePasswordRequest("oldPass", "newPass");
        when(passwordEncoder.matches("oldPass", "encoded_password")).thenReturn(true);
        when(passwordEncoder.encode("newPass")).thenReturn("new_encoded_password");

        // Act
        assertDoesNotThrow(() -> authService.changePassword(request));

        // Assert
        assertEquals("new_encoded_password", mockUser.getPasswordHash());
        verify(userRepository, times(1)).save(mockUser);
    }
}
