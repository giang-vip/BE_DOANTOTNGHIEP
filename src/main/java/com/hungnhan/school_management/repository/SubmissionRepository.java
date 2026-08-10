package com.hungnhan.school_management.repository;

import com.hungnhan.school_management.entity.Submission;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SubmissionRepository extends JpaRepository<Submission, Long> {
    Page<Submission> findByAssignmentId(Long assignmentId, Pageable pageable);
    
    Optional<Submission> findByAssignmentIdAndEnrollmentId(Long assignmentId, Long enrollmentId);
    
    boolean existsByAssignmentIdAndEnrollmentId(Long assignmentId, Long enrollmentId);
}
