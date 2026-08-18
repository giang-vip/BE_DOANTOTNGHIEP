package com.hungnhan.school_management.repository;

import com.hungnhan.school_management.entity.Major;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MajorRepository extends JpaRepository<Major, Long> {
    Optional<Major> findByCode(String code);
    boolean existsByCode(String code);

    @org.springframework.data.jpa.repository.Query("SELECT m FROM Major m " +
           "WHERE (:departmentId IS NULL OR m.department.id = :departmentId) " +
           "AND (:search IS NULL OR LOWER(m.code) LIKE LOWER(CONCAT('%', :search, '%')) OR LOWER(m.name) LIKE LOWER(CONCAT('%', :search, '%')))")
    org.springframework.data.domain.Page<Major> searchMajors(@org.springframework.data.repository.query.Param("search") String search, @org.springframework.data.repository.query.Param("departmentId") Long departmentId, org.springframework.data.domain.Pageable pageable);

    @org.springframework.data.jpa.repository.Query("SELECT m FROM Major m WHERE m.department.id = :departmentId")
    org.springframework.data.domain.Page<Major> findByDepartmentId(@org.springframework.data.repository.query.Param("departmentId") Long departmentId, org.springframework.data.domain.Pageable pageable);
}
