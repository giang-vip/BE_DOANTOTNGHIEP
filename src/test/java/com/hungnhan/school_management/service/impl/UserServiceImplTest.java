package com.hungnhan.school_management.service.impl;

import com.hungnhan.school_management.dto.request.UserCreationRequest;
import com.hungnhan.school_management.dto.request.UserUpdateRequest;
import com.hungnhan.school_management.dto.response.PageResponse;
import com.hungnhan.school_management.dto.response.UserResponse;
import com.hungnhan.school_management.entity.Role;
import com.hungnhan.school_management.entity.User;
import com.hungnhan.school_management.exception.AppException;
import com.hungnhan.school_management.exception.ErrorCode;
import com.hungnhan.school_management.mapper.UserMapper;
import com.hungnhan.school_management.repository.RoleRepository;
import com.hungnhan.school_management.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    private User mockUser;
    private Role mockRole;

    @BeforeEach
    void setUp() {
        mockRole = new Role();
        mockRole.setId(1L);
        mockRole.setName("STUDENT");

        mockUser = new User();
        mockUser.setId(1L);
        mockUser.setUsername("testuser");
        mockUser.setEmail("test@gmail.com");
        mockUser.setPasswordHash("hashed_pass");
        mockUser.setRoles(Set.of(mockRole));
    }

    @Test
    void createUser_Success() {
        UserCreationRequest request = new UserCreationRequest();
        request.setUsername("testuser");
        request.setEmail("test@gmail.com");
        request.setPassword("123456");
        request.setRoles(Set.of("STUDENT"));

        Mockito.when(userRepository.existsByUsername("testuser")).thenReturn(false);
        Mockito.when(userRepository.existsByEmail("test@gmail.com")).thenReturn(false);
        Mockito.when(roleRepository.findByName("STUDENT")).thenReturn(Optional.of(mockRole));
        Mockito.when(userMapper.toUser(request)).thenReturn(mockUser);
        Mockito.when(passwordEncoder.encode(request.getPassword())).thenReturn("hashed_pass");
        Mockito.when(userRepository.save(any(User.class))).thenReturn(mockUser);
        
        UserResponse mockResponse = new UserResponse();
        mockResponse.setUsername("testuser");
        Mockito.when(userMapper.toUserResponse(mockUser)).thenReturn(mockResponse);

        UserResponse response = userService.createUser(request);

        assertNotNull(response);
        assertEquals("testuser", response.getUsername());
    }

    @Test
    void createUser_UsernameExisted_ThrowsException() {
        UserCreationRequest request = new UserCreationRequest();
        request.setUsername("testuser");
        request.setEmail("test2@gmail.com");

        Mockito.when(userRepository.existsByUsername("testuser")).thenReturn(true);

        AppException exception = assertThrows(AppException.class, () -> userService.createUser(request));
        assertEquals(ErrorCode.USER_EXISTED, exception.getErrorCode());
    }

    @Test
    void getUserById_Success() {
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));
        
        UserResponse mockResponse = new UserResponse();
        mockResponse.setId(1L);
        Mockito.when(userMapper.toUserResponse(mockUser)).thenReturn(mockResponse);

        UserResponse response = userService.getUserById(1L);

        assertNotNull(response);
        assertEquals(1L, response.getId());
    }

    @Test
    void getUserById_NotFound_ThrowsException() {
        Mockito.when(userRepository.findById(99L)).thenReturn(Optional.empty());

        AppException exception = assertThrows(AppException.class, () -> userService.getUserById(99L));
        assertEquals(ErrorCode.USER_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    void updateUser_Success() {
        UserUpdateRequest request = new UserUpdateRequest();
        request.setEmail("new@gmail.com");
        request.setRoles(Set.of("STUDENT"));

        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));
        Mockito.when(roleRepository.findByName("STUDENT")).thenReturn(Optional.of(mockRole));
        Mockito.when(userRepository.save(mockUser)).thenReturn(mockUser);
        
        UserResponse mockResponse = new UserResponse();
        mockResponse.setEmail("new@gmail.com");
        Mockito.when(userMapper.toUserResponse(mockUser)).thenReturn(mockResponse);

        UserResponse response = userService.updateUser(1L, request);

        assertNotNull(response);
        assertEquals("new@gmail.com", response.getEmail());
    }

    @Test
    void deleteUser_Success() {
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));
        Mockito.doNothing().when(userRepository).delete(mockUser);

        assertDoesNotThrow(() -> userService.deleteUser(1L));
        Mockito.verify(userRepository, Mockito.times(1)).delete(mockUser);
    }

    @Test
    void deleteUser_NotFound_ThrowsException() {
        Mockito.when(userRepository.findById(99L)).thenReturn(Optional.empty());

        AppException exception = assertThrows(AppException.class, () -> userService.deleteUser(99L));
        assertEquals(ErrorCode.USER_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    void getUsers_Success() {
        Page<User> pageMock = new PageImpl<>(List.of(mockUser));
        Mockito.when(userRepository.searchUsers(any(), any(), any(Pageable.class))).thenReturn(pageMock);
        
        UserResponse mockResponse = new UserResponse();
        Mockito.when(userMapper.toUserResponse(any(User.class))).thenReturn(mockResponse);

        PageResponse<UserResponse> response = userService.getUsers(null, null, 0, 10);

        assertNotNull(response);
        assertEquals(1, response.getContent().size());
        assertEquals(1, response.getTotalElements());
    }
}
