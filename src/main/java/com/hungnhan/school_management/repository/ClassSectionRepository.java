package com.hungnhan.school_management.repository;

import com.hungnhan.school_management.entity.ClassSection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ClassSectionRepository extends JpaRepository<ClassSection, Long> {
    Optional<ClassSection> findBySectionCode(String sectionCode);

    boolean existsBySectionCode(String sectionCode);

    @Query("SELECT c FROM ClassSection c " +
           "LEFT JOIN c.subject s " +
           "LEFT JOIN c.semester sm " +
           "LEFT JOIN c.department d " +
           "LEFT JOIN c.major m " +
           "WHERE (:semesterId IS NULL OR sm.id = :semesterId) " +
           "AND (:subjectId IS NULL OR s.id = :subjectId) " +
           "AND (:departmentId IS NULL OR d.id = :departmentId) " +
           "AND (:majorId IS NULL OR m.id = :majorId) " +
           "AND (:search IS NULL OR LOWER(c.sectionCode) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<ClassSection> searchClassSections(@Param("search") String search, @Param("semesterId") Long semesterId, @Param("subjectId") Long subjectId, @Param("departmentId") Long departmentId, @Param("majorId") Long majorId, Pageable pageable);

    @Query("SELECT c FROM ClassSection c " +
           "LEFT JOIN c.subject s " +
           "LEFT JOIN c.semester sm " +
           "LEFT JOIN c.major m " +
           "WHERE (:semesterId IS NULL OR sm.id = :semesterId) " +
           "AND (:majorId IS NULL OR m.id = :majorId OR m.id IS NULL) " +
           "AND (:search IS NULL OR LOWER(c.sectionCode) LIKE LOWER(CONCAT('%', :search, '%')) OR LOWER(s.name) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<ClassSection> searchAvailableClassesForStudent(@Param("semesterId") Long semesterId, @Param("majorId") Long majorId, @Param("search") String search, Pageable pageable);

    @Query("SELECT c FROM ClassSection c " +
           "LEFT JOIN c.subject s " +
           "LEFT JOIN c.semester sm " +
           "WHERE c.teacher.id = :teacherId " +
           "AND (:semesterId IS NULL OR sm.id = :semesterId) " +
           "AND (:search IS NULL OR LOWER(c.sectionCode) LIKE LOWER(CONCAT('%', :search, '%')) OR LOWER(s.name) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<ClassSection> searchTeacherClassSections(@Param("teacherId") Long teacherId, @Param("search") String search, @Param("semesterId") Long semesterId, Pageable pageable);

    @Query("SELECT COUNT(c) > 0 FROM ClassSection c " +
           "WHERE c.room = :room " +
           "AND c.weekday = :weekday " +
           "AND ((c.startDate <= :endDate AND c.endDate >= :startDate)) " +
           "AND ((c.startTime <= :endTime AND c.endTime >= :startTime)) " +
           "AND (:excludeId IS NULL OR c.id <> :excludeId)")
    boolean checkRoomConflict(@Param("room") String room, @Param("weekday") Integer weekday, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate, @Param("startTime") LocalTime startTime, @Param("endTime") LocalTime endTime, @Param("excludeId") Long excludeId);

    @Query("SELECT COUNT(c) > 0 FROM ClassSection c " +
           "WHERE c.teacher.id = :teacherId " +
           "AND c.weekday = :weekday " +
           "AND ((c.startDate <= :endDate AND c.endDate >= :startDate)) " +
           "AND ((c.startTime <= :endTime AND c.endTime >= :startTime)) " +
           "AND (:excludeId IS NULL OR c.id <> :excludeId)")
    boolean checkTeacherConflict(@Param("teacherId") Long teacherId, @Param("weekday") Integer weekday, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate, @Param("startTime") LocalTime startTime, @Param("endTime") LocalTime endTime, @Param("excludeId") Long excludeId);
}
