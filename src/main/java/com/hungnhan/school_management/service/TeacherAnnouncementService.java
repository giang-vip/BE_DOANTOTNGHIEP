package com.hungnhan.school_management.service;

import com.hungnhan.school_management.dto.request.AnnouncementRequest;
import com.hungnhan.school_management.dto.response.AnnouncementResponse;
import com.hungnhan.school_management.dto.response.PageResponse;

public interface TeacherAnnouncementService {
    PageResponse<AnnouncementResponse> getAnnouncements(String username, Long classSectionId, int page, int size);
    
    AnnouncementResponse createAnnouncement(String username, Long classSectionId, AnnouncementRequest request);
}
