package com.hungnhan.school_management.config;

import com.hungnhan.school_management.constant.Gender;
import com.hungnhan.school_management.constant.UserStatus;
import com.hungnhan.school_management.entity.Role;
import com.hungnhan.school_management.entity.User;
import com.hungnhan.school_management.repository.RoleRepository;
import com.hungnhan.school_management.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

        private final UserRepository userRepository;
        private final RoleRepository roleRepository;
        private final PasswordEncoder passwordEncoder;

        @Override
        public void run(String... args) throws Exception {
                // 1. Tạo các roles cơ bản nếu chưa có
                Role adminRole = roleRepository.findByName("ADMIN")
                                .orElseGet(() -> roleRepository.save(Role.builder().name("ADMIN")
                                                .description("Quản trị viên").build()));

                roleRepository.findByName("TEACHER")
                                .orElseGet(() -> roleRepository.save(
                                                Role.builder().name("TEACHER").description("Giảng viên").build()));

                roleRepository.findByName("STUDENT")
                                .orElseGet(() -> roleRepository.save(
                                                Role.builder().name("STUDENT").description("Sinh viên").build()));

                // 2. Tạo tài khoản admin mặc định nếu chưa có
                if (!userRepository.existsByUsername("admin")) {
                        User adminUser = User.builder()
                                        .username("admin")
                                        .passwordHash(passwordEncoder.encode("123456"))
                                        .email("admin@school.edu.vn")
                                        .fullName("Quản trị viên Hệ thống")
                                        .status(UserStatus.ACTIVE)
                                        .gender(Gender.MALE)
                                        .roles(Set.of(adminRole))
                                        .build();

                        userRepository.save(adminUser);
                        System.out.println("✅ Đã tự động khởi tạo tài khoản ADMIN (username: admin, password: 123456)");
                }
        }
}
