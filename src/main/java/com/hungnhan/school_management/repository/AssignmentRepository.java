package com.hungnhan.school_management.repository;

import com.hungnhan.school_management.entity.Assignment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssignmentRepository extends JpaRepository<Assignment, Long> {
    Page<Assignment> findByClassSectionIdOrderByDueAtAsc(Long classSectionId, Pageable pageable);
}
