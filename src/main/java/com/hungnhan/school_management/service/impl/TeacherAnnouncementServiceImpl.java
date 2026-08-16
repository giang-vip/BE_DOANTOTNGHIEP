package com.hungnhan.school_management.service.impl;

import com.hungnhan.school_management.dto.request.AnnouncementRequest;
import com.hungnhan.school_management.dto.response.AnnouncementResponse;
import com.hungnhan.school_management.dto.response.PageResponse;
import com.hungnhan.school_management.entity.Announcement;
import com.hungnhan.school_management.entity.ClassSection;
import com.hungnhan.school_management.entity.Teacher;
import com.hungnhan.school_management.entity.User;
import com.hungnhan.school_management.exception.AppException;
import com.hungnhan.school_management.exception.ErrorCode;
import com.hungnhan.school_management.mapper.AnnouncementMapper;
import com.hungnhan.school_management.repository.AnnouncementRepository;
import com.hungnhan.school_management.repository.ClassSectionRepository;
import com.hungnhan.school_management.repository.TeacherRepository;
import com.hungnhan.school_management.repository.UserRepository;
import com.hungnhan.school_management.service.TeacherAnnouncementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class TeacherAnnouncementServiceImpl implements TeacherAnnouncementService {

    private final AnnouncementRepository announcementRepository;
    private final ClassSectionRepository classSectionRepository;
    private final UserRepository userRepository;
    private final TeacherRepository teacherRepository;
    private final AnnouncementMapper announcementMapper;

    private User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
    }

    private void checkTeacherPermission(User user, ClassSection classSection) {
        Teacher teacher = teacherRepository.findByUserId(user.getId())
                .orElseThrow(() -> new AppException(ErrorCode.TEACHER_NOT_FOUND));

        if (!classSection.getTeacher().getId().equals(teacher.getId())) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }
    }

    @Override
    public PageResponse<AnnouncementResponse> getAnnouncements(String username, Long classSectionId, int page, int size) {
        User user = getUserByUsername(username);
        ClassSection classSection = classSectionRepository.findById(classSectionId)
                .orElseThrow(() -> new AppException(ErrorCode.CLASS_SECTION_NOT_FOUND));

        checkTeacherPermission(user, classSection);

        Pageable pageable = PageRequest.of(page, size);
        Page<Announcement> announcementPage = announcementRepository.findByClassSectionIdOrderByCreatedAtDesc(classSectionId, pageable);

        List<AnnouncementResponse> content = announcementPage.getContent().stream()
                .map(announcementMapper::toAnnouncementResponse)
                .collect(Collectors.toList());

        return PageResponse.<AnnouncementResponse>builder()
                .content(content)
                .pageNumber(announcementPage.getNumber())
                .pageSize(announcementPage.getSize())
                .totalElements(announcementPage.getTotalElements())
                .totalPages(announcementPage.getTotalPages())
                .last(announcementPage.isLast())
                .build();
    }

    @Override
    public AnnouncementResponse createAnnouncement(String username, Long classSectionId, AnnouncementRequest request) {
        User user = getUserByUsername(username);
        ClassSection classSection = classSectionRepository.findById(classSectionId)
                .orElseThrow(() -> new AppException(ErrorCode.CLASS_SECTION_NOT_FOUND));

        checkTeacherPermission(user, classSection);

        Announcement announcement = announcementMapper.toAnnouncement(request);
        announcement.setClassSection(classSection);
        announcement.setCreatedBy(user);

        return announcementMapper.toAnnouncementResponse(announcementRepository.save(announcement));
    }

    @Override
    public AnnouncementResponse updateAnnouncement(String username, Long classSectionId, Long announcementId, AnnouncementRequest request) {
        User user = getUserByUsername(username);
        ClassSection classSection = classSectionRepository.findById(classSectionId)
                .orElseThrow(() -> new AppException(ErrorCode.CLASS_SECTION_NOT_FOUND));

        checkTeacherPermission(user, classSection);

        Announcement announcement = announcementRepository.findById(announcementId)
                .orElseThrow(() -> new RuntimeException("Announcement not found"));

        announcement.setTitle(request.getTitle());
        announcement.setContent(request.getContent());

        return announcementMapper.toAnnouncementResponse(announcementRepository.save(announcement));
    }

    @Override
    public void deleteAnnouncement(String username, Long classSectionId, Long announcementId) {
        User user = getUserByUsername(username);
        ClassSection classSection = classSectionRepository.findById(classSectionId)
                .orElseThrow(() -> new AppException(ErrorCode.CLASS_SECTION_NOT_FOUND));

        checkTeacherPermission(user, classSection);

        Announcement announcement = announcementRepository.findById(announcementId)
                .orElseThrow(() -> new RuntimeException("Announcement not found"));

        announcementRepository.delete(announcement);
    }
}
