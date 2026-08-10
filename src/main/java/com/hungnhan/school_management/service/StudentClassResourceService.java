package com.hungnhan.school_management.service;

import com.hungnhan.school_management.dto.response.AttendanceRecordResponse;
import com.hungnhan.school_management.dto.response.LearningMaterialResponse;
import com.hungnhan.school_management.dto.response.PageResponse;

import java.util.List;

public interface StudentClassResourceService {

    List<AttendanceRecordResponse> getMyAttendance(String username, Long classSectionId);

    PageResponse<LearningMaterialResponse> getMyMaterials(String username, Long classSectionId, int page, int size);
}
