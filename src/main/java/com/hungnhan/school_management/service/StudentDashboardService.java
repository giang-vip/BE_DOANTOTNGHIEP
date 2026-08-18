package com.hungnhan.school_management.service;

import com.hungnhan.school_management.dto.response.AnnouncementResponse;
import com.hungnhan.school_management.dto.response.ClassSectionResponse;
import com.hungnhan.school_management.dto.response.PageResponse;
import com.hungnhan.school_management.dto.response.RegistrationPeriodResponse;
import com.hungnhan.school_management.dto.response.EnrollmentResponse;

import java.util.List;

public interface StudentDashboardService {

    PageResponse<ClassSectionResponse> getStudentClasses(String username, int page, int size);

    PageResponse<AnnouncementResponse> getStudentAnnouncements(String username, int page, int size);

    RegistrationPeriodResponse getCurrentRegistrationPeriod();
    
    List<com.hungnhan.school_management.dto.response.StudentCurriculumResponse> getStudentCurriculum(String username);

    PageResponse<ClassSectionResponse> getAvailableClasses(String username, String search, Long semesterId, int page, int size);

    EnrollmentResponse enrollClass(String username, Long classSectionId);

    void dropClass(String username, Long classSectionId);
}
