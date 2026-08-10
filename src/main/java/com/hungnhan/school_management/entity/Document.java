package com.hungnhan.school_management.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Bang luu file dinh kem chung (polymorphic): owner_type + owner_id
 * xac dinh doi tuong so huu file (VD: "ASSIGNMENT", "STUDENT_FACE_PHOTO"...).
 * Khong dung @ManyToOne vi owner co the la nhieu loai entity khac nhau.
 */
@Entity
@Table(name = "documents")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "owner_type", nullable = false, length = 50)
    private String ownerType;

    @Column(name = "owner_id", nullable = false)
    private Long ownerId;

    @Column(name = "file_name", nullable = false)
    private String fileName;

    @Column(name = "storage_key", nullable = false, length = 500)
    private String storageKey;

    @Column(name = "mime_type", length = 100)
    private String mimeType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uploaded_by", nullable = false)
    private User uploadedBy;

    @Column(name = "uploaded_at", updatable = false)
    private LocalDateTime uploadedAt;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private DocumentStatus status = DocumentStatus.UPLOADED;

    @PrePersist
    void prePersist() {
        uploadedAt = LocalDateTime.now();
    }

    public enum DocumentStatus {
        UPLOADED, PROCESSING, PROCESSED, FAILED
    }
}
