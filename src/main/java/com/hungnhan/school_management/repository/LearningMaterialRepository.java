package com.hungnhan.school_management.repository;

import com.hungnhan.school_management.entity.LearningMaterial;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LearningMaterialRepository extends JpaRepository<LearningMaterial, Long> {
    Page<LearningMaterial> findByClassSectionIdOrderByUploadedAtDesc(Long classSectionId, Pageable pageable);
}
