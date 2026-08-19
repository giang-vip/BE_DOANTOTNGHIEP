package com.hungnhan.school_management.service;

import com.hungnhan.school_management.dto.request.DocumentRequest;
import com.hungnhan.school_management.dto.response.DocumentResponse;
import com.hungnhan.school_management.dto.response.PageResponse;

public interface SubjectMaterialService {
    PageResponse<DocumentResponse> getMaterials(Long subjectId, int page, int size);

    DocumentResponse uploadMaterial(String username, Long subjectId, DocumentRequest request);

    void deleteMaterial(Long id);
}
