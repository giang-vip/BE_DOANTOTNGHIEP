package com.hungnhan.school_management.repository;

import com.hungnhan.school_management.entity.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    Optional<Student> findByStudentCode(String studentCode);
    
    Optional<Student> findByUserId(Long userId);

    boolean existsByStudentCode(String studentCode);
    
    boolean existsByUserId(Long userId);

    @Query("SELECT s FROM Student s " +
           "LEFT JOIN s.major m " +
           "LEFT JOIN m.department d " +
           "LEFT JOIN s.schoolClass c " +
           "WHERE (:departmentId IS NULL OR d.id = :departmentId) " +
           "AND (:majorId IS NULL OR m.id = :majorId) " +
           "AND (:classId IS NULL OR c.id = :classId) " +
           "AND (:search IS NULL OR LOWER(s.studentCode) LIKE LOWER(CONCAT('%', :search, '%')) OR LOWER(s.fullName) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<Student> searchStudents(@Param("search") String search, @Param("departmentId") Long departmentId, @Param("majorId") Long majorId, @Param("classId") Long classId, Pageable pageable);
}
