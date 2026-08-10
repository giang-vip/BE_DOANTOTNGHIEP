package com.hungnhan.school_management.repository;

import com.hungnhan.school_management.entity.Department;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {
    Optional<Department> findByCode(String code);

    boolean existsByCode(String code);

    @Query("SELECT d FROM Department d WHERE (:search IS NULL OR LOWER(d.code) LIKE LOWER(CONCAT('%', :search, '%')) OR LOWER(d.name) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<Department> searchDepartments(@Param("search") String search, Pageable pageable);
}
