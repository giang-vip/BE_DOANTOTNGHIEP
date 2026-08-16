package com.hungnhan.school_management.repository;

import com.hungnhan.school_management.entity.AttendanceSession;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.EntityGraph;
import java.util.Optional;

@Repository
public interface AttendanceSessionRepository extends JpaRepository<AttendanceSession, Long> {
    
    boolean existsByClassSectionIdAndSessionDate(Long classSectionId, LocalDate sessionDate);
    
    @EntityGraph(attributePaths = {"classSection", "createdBy"})
    Optional<AttendanceSession> findById(Long id);
    
    @EntityGraph(attributePaths = {"classSection", "createdBy"})
    Page<AttendanceSession> findByClassSectionIdOrderBySessionDateDesc(Long classSectionId, Pageable pageable);
}
