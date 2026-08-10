package com.hungnhan.school_management.repository;

import com.hungnhan.school_management.entity.Enrollment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    
    Optional<Enrollment> findByStudentIdAndClassSectionId(Long studentId, Long classSectionId);

    boolean existsByStudentIdAndClassSectionId(Long studentId, Long classSectionId);

    @Query("SELECT COUNT(e) FROM Enrollment e WHERE e.classSection.id = :classSectionId AND e.status = 'ACTIVE'")
    long countActiveEnrollmentsByClassSectionId(@Param("classSectionId") Long classSectionId);

    @Query("SELECT e FROM Enrollment e " +
           "WHERE (:studentId IS NULL OR e.student.id = :studentId) " +
           "AND (:classSectionId IS NULL OR e.classSection.id = :classSectionId)")
    Page<Enrollment> searchEnrollments(@Param("studentId") Long studentId, @Param("classSectionId") Long classSectionId, Pageable pageable);
}
