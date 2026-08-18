package com.hungnhan.school_management.repository;

import com.hungnhan.school_management.entity.Subject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Long> {
    Optional<Subject> findByCode(String code);

    boolean existsByCode(String code);

    @Query("SELECT DISTINCT s FROM Subject s " +
           "LEFT JOIN MajorSubject ms ON ms.subject.id = s.id " +
           "WHERE (:departmentId IS NULL OR s.department.id = :departmentId) " +
           "AND (:majorId IS NULL OR ms.major.id = :majorId) " +
           "AND (:search IS NULL OR LOWER(s.code) LIKE LOWER(CONCAT('%', :search, '%')) OR LOWER(s.name) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<Subject> searchSubjects(@Param("search") String search, @Param("departmentId") Long departmentId, @Param("majorId") Long majorId, Pageable pageable);

    @Query("SELECT COUNT(ms) FROM MajorSubject ms WHERE ms.major.id = :majorId")
    long countByMajorId(@Param("majorId") Long majorId);
}
