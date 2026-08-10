package com.hungnhan.school_management.service;

import com.hungnhan.school_management.dto.request.AdminAnnouncementRequest;
import com.hungnhan.school_management.dto.response.AdminAnnouncementResponse;

import java.util.List;

public interface AdminAnnouncementService {
    AdminAnnouncementResponse createAnnouncement(AdminAnnouncementRequest request, String username);
    List<AdminAnnouncementResponse> getAllAnnouncements();
    void deleteAnnouncement(Long id);
}
