package com.hungnhan.school_management.repository;

import com.hungnhan.school_management.entity.AcademicYear;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AcademicYearRepository extends JpaRepository<AcademicYear, Long> {
    Optional<AcademicYear> findByCode(String code);

    boolean existsByCode(String code);

    @Query("SELECT a FROM AcademicYear a WHERE (:search IS NULL OR LOWER(a.code) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<AcademicYear> searchAcademicYears(@Param("search") String search, Pageable pageable);
}
