package com.hungnhan.school_management.repository;

import com.hungnhan.school_management.entity.Semester;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SemesterRepository extends JpaRepository<Semester, Long> {
    Optional<Semester> findByAcademicYearIdAndCode(Long academicYearId, String code);

    boolean existsByAcademicYearIdAndCode(Long academicYearId, String code);

    @Query("SELECT s FROM Semester s WHERE (:academicYearId IS NULL OR s.academicYear.id = :academicYearId) " +
           "AND (:search IS NULL OR LOWER(s.code) LIKE LOWER(CONCAT('%', :search, '%')) OR LOWER(s.name) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<Semester> searchSemesters(@Param("search") String search, @Param("academicYearId") Long academicYearId, Pageable pageable);
}
