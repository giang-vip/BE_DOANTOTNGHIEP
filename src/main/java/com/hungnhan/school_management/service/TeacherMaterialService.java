package com.hungnhan.school_management.service;

import com.hungnhan.school_management.dto.request.LearningMaterialRequest;
import com.hungnhan.school_management.dto.response.LearningMaterialResponse;
import com.hungnhan.school_management.dto.response.PageResponse;

public interface TeacherMaterialService {
    PageResponse<LearningMaterialResponse> getMaterials(String username, Long classSectionId, int page, int size);
    
    LearningMaterialResponse uploadMaterial(String username, Long classSectionId, LearningMaterialRequest request);
    
    void deleteMaterial(String username, Long id);
}
