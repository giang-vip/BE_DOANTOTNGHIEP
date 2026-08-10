package com.hungnhan.school_management.repository;

import com.hungnhan.school_management.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // Tìm kiếm user theo username (phục vụ đăng nhập/kiểm tra trùng)
    Optional<User> findByUsername(String username);

    // Kiểm tra sự tồn tại của username và email
    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    @Query("SELECT DISTINCT u FROM User u " +
           "LEFT JOIN u.roles r " +
           "WHERE (:search IS NULL OR LOWER(u.username) LIKE LOWER(CONCAT('%', :search, '%')) " +
           "      OR LOWER(u.fullName) LIKE LOWER(CONCAT('%', :search, '%'))) " +
           "AND (:roleName IS NULL OR r.name = :roleName) " +
           "AND (:status IS NULL OR u.status = :status)")
    Page<User> searchUsers(@Param("search") String search, @Param("roleName") String roleName, @Param("status") com.hungnhan.school_management.constant.UserStatus status, Pageable pageable);
}
