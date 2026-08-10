package com.hungnhan.school_management.repository;

import com.hungnhan.school_management.entity.Teacher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Long> {
    Optional<Teacher> findByTeacherCode(String teacherCode);
    
    Optional<Teacher> findByUserId(Long userId);

    boolean existsByTeacherCode(String teacherCode);

    boolean existsByUserId(Long userId);

    @Query("SELECT t FROM Teacher t " +
           "LEFT JOIN t.department d " +
           "WHERE (:departmentId IS NULL OR d.id = :departmentId) " +
           "AND (:search IS NULL OR LOWER(t.teacherCode) LIKE LOWER(CONCAT('%', :search, '%')) OR LOWER(t.fullName) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<Teacher> searchTeachers(@Param("search") String search, @Param("departmentId") Long departmentId, Pageable pageable);
}
