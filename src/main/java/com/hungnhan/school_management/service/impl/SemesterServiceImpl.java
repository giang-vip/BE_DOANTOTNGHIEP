package com.hungnhan.school_management.service.impl;

import com.hungnhan.school_management.dto.request.SemesterRequest;
import com.hungnhan.school_management.dto.response.PageResponse;
import com.hungnhan.school_management.dto.response.SemesterResponse;
import com.hungnhan.school_management.entity.AcademicYear;
import com.hungnhan.school_management.entity.Semester;
import com.hungnhan.school_management.exception.AppException;
import com.hungnhan.school_management.exception.ErrorCode;
import com.hungnhan.school_management.mapper.SemesterMapper;
import com.hungnhan.school_management.repository.AcademicYearRepository;
import com.hungnhan.school_management.repository.SemesterRepository;
import com.hungnhan.school_management.service.SemesterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
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
public class SemesterServiceImpl implements SemesterService {

    private final SemesterRepository semesterRepository;
    private final AcademicYearRepository academicYearRepository;
    private final SemesterMapper semesterMapper;

    @Override
    public SemesterResponse createSemester(SemesterRequest request) {
        if (request.getStartDate().isAfter(request.getEndDate()) || request.getStartDate().isEqual(request.getEndDate())) {
            throw new AppException(ErrorCode.SEMESTER_INVALID_DATES);
        }

        AcademicYear academicYear = academicYearRepository.findById(request.getAcademicYearId())
                .orElseThrow(() -> new AppException(ErrorCode.ACADEMIC_YEAR_NOT_FOUND));

        if (semesterRepository.existsByAcademicYearIdAndCode(request.getAcademicYearId(), request.getCode())) {
            throw new AppException(ErrorCode.SEMESTER_EXISTED);
        }

        Semester semester = semesterMapper.toSemester(request);
        semester.setAcademicYear(academicYear);
        if (semester.getIsCurrent() == null) {
            semester.setIsCurrent(false);
        }

        return semesterMapper.toSemesterResponse(semesterRepository.save(semester));
    }

    @Override
    public SemesterResponse updateSemester(Long id, SemesterRequest request) {
        if (request.getStartDate().isAfter(request.getEndDate()) || request.getStartDate().isEqual(request.getEndDate())) {
            throw new AppException(ErrorCode.SEMESTER_INVALID_DATES);
        }

        Semester semester = semesterRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.SEMESTER_NOT_FOUND));

        AcademicYear academicYear = academicYearRepository.findById(request.getAcademicYearId())
                .orElseThrow(() -> new AppException(ErrorCode.ACADEMIC_YEAR_NOT_FOUND));

        if (!semester.getCode().equals(request.getCode()) || !semester.getAcademicYear().getId().equals(request.getAcademicYearId())) {
            if (semesterRepository.existsByAcademicYearIdAndCode(request.getAcademicYearId(), request.getCode())) {
                throw new AppException(ErrorCode.SEMESTER_EXISTED);
            }
        }

        semesterMapper.updateSemester(semester, request);
        semester.setAcademicYear(academicYear);
        if (semester.getIsCurrent() == null) {
            semester.setIsCurrent(false);
        }

        return semesterMapper.toSemesterResponse(semesterRepository.save(semester));
    }

    @Override
    public PageResponse<SemesterResponse> getSemesters(String search, Long academicYearId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Semester> semesterPage = semesterRepository.searchSemesters(search, academicYearId, pageable);

        List<SemesterResponse> content = semesterPage.getContent().stream()
                .map(semesterMapper::toSemesterResponse)
                .collect(Collectors.toList());

        return PageResponse.<SemesterResponse>builder()
                .content(content)
                .pageNumber(semesterPage.getNumber())
                .pageSize(semesterPage.getSize())
                .totalElements(semesterPage.getTotalElements())
                .totalPages(semesterPage.getTotalPages())
                .last(semesterPage.isLast())
                .build();
    }

    @Override
    public SemesterResponse getSemesterById(Long id) {
        Semester semester = semesterRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.SEMESTER_NOT_FOUND));
        return semesterMapper.toSemesterResponse(semester);
    }

    @Override
    public void deleteSemester(Long id) {
        Semester semester = semesterRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.SEMESTER_NOT_FOUND));
        try {
            semesterRepository.delete(semester);
            semesterRepository.flush();
        } catch (DataIntegrityViolationException ex) {
            log.error("Lỗi xóa học kỳ, đang có dữ liệu liên kết: ", ex);
            throw new AppException(ErrorCode.SEMESTER_HAS_REFERENCES);
        }
    }
}
