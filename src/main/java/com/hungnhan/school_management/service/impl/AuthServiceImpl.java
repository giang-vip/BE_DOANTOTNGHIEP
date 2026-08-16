package com.hungnhan.school_management.service.impl;

import com.hungnhan.school_management.dto.request.LoginRequest;
import com.hungnhan.school_management.dto.request.ChangePasswordRequest;
import com.hungnhan.school_management.dto.request.UpdateProfileRequest;
import com.hungnhan.school_management.dto.response.AuthResponse;
import com.hungnhan.school_management.dto.response.UserResponse;
import com.hungnhan.school_management.entity.InvalidatedToken;
import com.hungnhan.school_management.entity.Role;
import com.hungnhan.school_management.entity.User;
import com.hungnhan.school_management.exception.AppException;
import com.hungnhan.school_management.exception.ErrorCode;
import com.hungnhan.school_management.mapper.UserMapper;
import com.hungnhan.school_management.repository.InvalidatedTokenRepository;
import com.hungnhan.school_management.repository.UserRepository;
import com.hungnhan.school_management.repository.StudentRepository;
import com.hungnhan.school_management.repository.TeacherRepository;
import com.hungnhan.school_management.entity.Student;
import com.hungnhan.school_management.entity.Teacher;
import com.hungnhan.school_management.security.JwtTokenProvider;
import com.hungnhan.school_management.service.AuthService;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final InvalidatedTokenRepository invalidatedTokenRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;

    private void enrichUserInfo(AuthResponse.UserInfo userInfo, User user) {
        if (user.getRoles().stream().anyMatch(r -> r.getName().equals("STUDENT"))) {
            studentRepository.findByUserId(user.getId()).ifPresent(student -> {
                userInfo.setStudentCode(student.getStudentCode());
                userInfo.setMajorName(student.getMajor() != null ? student.getMajor().getName() : null);
                userInfo.setSchoolClassName(
                        student.getSchoolClass() != null ? student.getSchoolClass().getName() : null);
                userInfo.setGpa(student.getGpa());
                userInfo.setTotalCredits(student.getTotalCredits());
                userInfo.setDateOfBirth(student.getDateOfBirth() != null ? student.getDateOfBirth().toString() : null);
            });
        } else if (user.getRoles().stream().anyMatch(r -> r.getName().equals("TEACHER"))) {
            teacherRepository.findByUserId(user.getId()).ifPresent(teacher -> {
                userInfo.setTeacherCode(teacher.getTeacherCode());
                userInfo.setDepartmentName(teacher.getDepartment() != null ? teacher.getDepartment().getName() : null);
                userInfo.setTitle(teacher.getTitle());
            });
        }
    }

    private void enrichUserResponse(UserResponse userResponse, User user) {
        if (user.getRoles().stream().anyMatch(r -> r.getName().equals("STUDENT"))) {
            studentRepository.findByUserId(user.getId()).ifPresent(student -> {
                userResponse.setStudentCode(student.getStudentCode());
                userResponse.setMajorName(student.getMajor() != null ? student.getMajor().getName() : null);
                userResponse.setSchoolClassName(
                        student.getSchoolClass() != null ? student.getSchoolClass().getName() : null);
                userResponse.setGpa(student.getGpa());
                userResponse.setTotalCredits(student.getTotalCredits());
                userResponse.setDateOfBirth(student.getDateOfBirth() != null ? student.getDateOfBirth().toString() : null);
            });
        } else if (user.getRoles().stream().anyMatch(r -> r.getName().equals("TEACHER"))) {
            teacherRepository.findByUserId(user.getId()).ifPresent(teacher -> {
                userResponse.setTeacherCode(teacher.getTeacherCode());
                userResponse
                        .setDepartmentName(teacher.getDepartment() != null ? teacher.getDepartment().getName() : null);
                userResponse.setTitle(teacher.getTitle());
            });
        }
    }

    @Override
    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request) {
        // 1. Tìm User theo username
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new AppException(ErrorCode.UNAUTHENTICATED));

        // 2. Kiểm tra mật khẩu
        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        // 3. Sinh token JWT
        String token = jwtTokenProvider.generateToken(user);

        // 4. Lấy vai trò chính
        String mainRole = user.getRoles().stream()
                .map(Role::getName)
                .findFirst()
                .orElse("STUDENT");

        // 5. Build UserInfo
        AuthResponse.UserInfo userInfo = AuthResponse.UserInfo.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .avatarUrl(user.getAvatarUrl())
                .build();
        enrichUserInfo(userInfo, user);

        return AuthResponse.builder()
                .token(token)
                .refreshToken(UUID.randomUUID().toString()) // Giả lập refresh token
                .role(mainRole)
                .userInfo(userInfo)
                .build();
    }

    @Override
    public void logout(String bearerToken) {
        try {
            String token = bearerToken;
            if (bearerToken.startsWith("Bearer ")) {
                token = bearerToken.substring(7);
            }

            Claims claims = jwtTokenProvider.getClaimsFromJWT(token);
            String tokenId = claims.getId();
            java.util.Date expiryTime = claims.getExpiration();

            InvalidatedToken invalidatedToken = InvalidatedToken.builder()
                    .id(tokenId)
                    .expiryTime(expiryTime)
                    .build();

            invalidatedTokenRepository.save(invalidatedToken);
        } catch (Exception e) {
            log.error("Lỗi khi đăng xuất và hủy token", e);
            // Vẫn cho phép hoàn tất (không quẳng lỗi hoặc quẳng lỗi tùy nghiệp vụ)
        }
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse getMe() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        String username = authentication.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        UserResponse userResponse = userMapper.toUserResponse(user);
        enrichUserResponse(userResponse, user);
        return userResponse;
    }

    @Override
    @Transactional
    public void changePassword(ChangePasswordRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        String username = authentication.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        // Kiểm tra mật khẩu cũ
        if (!passwordEncoder.matches(request.getOldPassword(), user.getPasswordHash())) {
            throw new AppException(ErrorCode.OLD_PASSWORD_INCORRECT);
        }

        // Cập nhật mật khẩu mới
        user.setPasswordHash(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }

    @Override
    @Transactional
    public UserResponse updateProfile(UpdateProfileRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        String username = authentication.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        if (request.getEmail() != null && !request.getEmail().isEmpty()) {
            user.setEmail(request.getEmail());
        }
        if (request.getPhone() != null && !request.getPhone().isEmpty()) {
            user.setPhone(request.getPhone());
        }

        userRepository.save(user);

        UserResponse userResponse = userMapper.toUserResponse(user);
        enrichUserResponse(userResponse, user);
        return userResponse;
    }
}
