package com.hungnhan.school_management.repository;

import com.hungnhan.school_management.entity.Announcement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnnouncementRepository extends JpaRepository<Announcement, Long> {
    Page<Announcement> findByClassSectionIdOrderByCreatedAtDesc(Long classSectionId, Pageable pageable);
    
    Page<Announcement> findByClassSectionIdInOrClassSectionIsNullOrderByCreatedAtDesc(java.util.List<Long> classSectionIds, Pageable pageable);
}
