package com.hungnhan.school_management.repository;

import com.hungnhan.school_management.entity.SchoolClass;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SchoolClassRepository extends JpaRepository<SchoolClass, Long> {
    Optional<SchoolClass> findByCode(String code);

    boolean existsByCode(String code);

    @Query("SELECT c FROM SchoolClass c " +
           "LEFT JOIN c.major m " +
           "WHERE (:majorId IS NULL OR m.id = :majorId) " +
           "AND (:search IS NULL OR LOWER(c.code) LIKE LOWER(CONCAT('%', :search, '%')) OR LOWER(c.name) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<SchoolClass> searchClasses(@Param("search") String search, @Param("majorId") Long majorId, Pageable pageable);
}
