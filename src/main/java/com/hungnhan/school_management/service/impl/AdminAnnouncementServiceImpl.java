package com.hungnhan.school_management.service.impl;

import com.hungnhan.school_management.dto.request.AdminAnnouncementRequest;
import com.hungnhan.school_management.dto.response.AdminAnnouncementResponse;
import com.hungnhan.school_management.entity.Announcement;
import com.hungnhan.school_management.entity.User;
import com.hungnhan.school_management.exception.AppException;
import com.hungnhan.school_management.exception.ErrorCode;
import com.hungnhan.school_management.repository.AnnouncementRepository;
import com.hungnhan.school_management.repository.UserRepository;
import com.hungnhan.school_management.service.AdminAnnouncementService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminAnnouncementServiceImpl implements AdminAnnouncementService {

    private final AnnouncementRepository announcementRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public AdminAnnouncementResponse createAnnouncement(AdminAnnouncementRequest request, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        Announcement announcement = Announcement.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .recipientGroup(request.getRecipientGroup() != null ? request.getRecipientGroup() : "all")
                .createdBy(user)
                .build();

        announcement = announcementRepository.save(announcement);
        return mapToResponse(announcement);
    }

    @Override
    public List<AdminAnnouncementResponse> getAllAnnouncements() {
        // Find announcements that have no classSection (global announcements)
        return announcementRepository.findAll().stream()
                .filter(a -> a.getClassSection() == null)
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteAnnouncement(Long id) {
        Announcement announcement = announcementRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Announcement not found"));
        announcementRepository.delete(announcement);
    }

    private AdminAnnouncementResponse mapToResponse(Announcement a) {
        return AdminAnnouncementResponse.builder()
                .id(a.getId())
                .title(a.getTitle())
                .content(a.getContent())
                .recipientGroup(a.getRecipientGroup())
                .sender(a.getCreatedBy().getFullName())
                .createdAt(a.getCreatedAt())
                .build();
    }
}
