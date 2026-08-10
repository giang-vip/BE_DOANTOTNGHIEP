package com.hungnhan.school_management.service;

import com.hungnhan.school_management.dto.response.AnnouncementResponse;
import com.hungnhan.school_management.dto.response.ClassSectionResponse;
import com.hungnhan.school_management.dto.response.PageResponse;

public interface StudentDashboardService {

    PageResponse<ClassSectionResponse> getStudentClasses(String username, int page, int size);

    PageResponse<AnnouncementResponse> getStudentAnnouncements(String username, int page, int size);
}
