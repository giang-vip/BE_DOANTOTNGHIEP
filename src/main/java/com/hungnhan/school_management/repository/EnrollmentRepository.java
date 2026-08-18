package com.hungnhan.school_management.repository;

import com.hungnhan.school_management.entity.Enrollment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
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

    @Query("SELECT e.classSection.id, COUNT(e) FROM Enrollment e WHERE e.classSection.id IN :classSectionIds AND e.status = 'ACTIVE' GROUP BY e.classSection.id")
    java.util.List<Object[]> countActiveEnrollmentsByClassSectionIds(@Param("classSectionIds") java.util.List<Long> classSectionIds);

    @EntityGraph(attributePaths = {
        "classSection",
        "classSection.subject",
        "classSection.teacher",
        "classSection.semester",
        "classSection.department",
        "classSection.major",
        "student"
    })
    @Query("SELECT e FROM Enrollment e " +
           "WHERE (:studentId IS NULL OR e.student.id = :studentId) " +
           "AND (:classSectionId IS NULL OR e.classSection.id = :classSectionId)")
    Page<Enrollment> searchEnrollments(@Param("studentId") Long studentId, @Param("classSectionId") Long classSectionId, Pageable pageable);

    @Query("SELECT e.finalGrade, COUNT(e) FROM Enrollment e " +
           "JOIN e.classSection cs " +
           "LEFT JOIN cs.semester sem " +
           "LEFT JOIN sem.academicYear y " +
           "LEFT JOIN cs.department d " +
           "LEFT JOIN cs.major m " +
           "WHERE (:yearId IS NULL OR y.id = :yearId) " +
           "AND (:semesterId IS NULL OR sem.id = :semesterId) " +
           "AND (:classSectionId IS NULL OR cs.id = :classSectionId) " +
           "AND (:departmentId IS NULL OR d.id = :departmentId) " +
           "AND (:majorId IS NULL OR m.id = :majorId) " +
           "AND e.finalGrade IS NOT NULL " +
           "GROUP BY e.finalGrade")
    java.util.List<Object[]> getGradeDistributionCounts(@Param("yearId") Long yearId, 
                                              @Param("semesterId") Long semesterId, 
                                              @Param("classSectionId") Long classSectionId, 
                                              @Param("departmentId") Long departmentId, 
                                              @Param("majorId") Long majorId);
}
