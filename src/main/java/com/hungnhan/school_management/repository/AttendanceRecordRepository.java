package com.hungnhan.school_management.repository;

import com.hungnhan.school_management.entity.AttendanceRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AttendanceRecordRepository extends JpaRepository<AttendanceRecord, Long> {
    
    List<AttendanceRecord> findByAttendanceSessionId(Long sessionId);
    
    Optional<AttendanceRecord> findByAttendanceSessionIdAndEnrollmentId(Long sessionId, Long enrollmentId);
    
    List<AttendanceRecord> findByEnrollmentId(Long enrollmentId);
}
