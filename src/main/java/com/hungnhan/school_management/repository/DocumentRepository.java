package com.hungnhan.school_management.repository;

import com.hungnhan.school_management.entity.Document;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {
    Page<Document> findByOwnerTypeAndOwnerIdOrderByUploadedAtDesc(String ownerType, Long ownerId, Pageable pageable);
}
