package com.hungnhan.school_management.repository;

import com.hungnhan.school_management.entity.MajorSubject;
import com.hungnhan.school_management.entity.MajorSubjectId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface MajorSubjectRepository extends JpaRepository<MajorSubject, MajorSubjectId> {
    Optional<MajorSubject> findByMajorIdAndSubjectId(Long majorId, Long subjectId);
    List<MajorSubject> findByMajorId(Long majorId);
    void deleteByMajorIdAndSubjectId(Long majorId, Long subjectId);
    void deleteBySubjectId(Long subjectId);
}
