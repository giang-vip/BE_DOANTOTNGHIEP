package com.hungnhan.school_management.service.impl;

import com.hungnhan.school_management.constant.ClassStatus;
import com.hungnhan.school_management.dto.request.SchoolClassRequest;
import com.hungnhan.school_management.dto.response.PageResponse;
import com.hungnhan.school_management.dto.response.SchoolClassResponse;
import com.hungnhan.school_management.entity.AcademicYear;
import com.hungnhan.school_management.entity.Major;
import com.hungnhan.school_management.entity.SchoolClass;
import com.hungnhan.school_management.entity.Teacher;
import com.hungnhan.school_management.exception.AppException;
import com.hungnhan.school_management.exception.ErrorCode;
import com.hungnhan.school_management.mapper.SchoolClassMapper;
import com.hungnhan.school_management.repository.AcademicYearRepository;
import com.hungnhan.school_management.repository.MajorRepository;
import com.hungnhan.school_management.repository.SchoolClassRepository;
import com.hungnhan.school_management.repository.TeacherRepository;
import com.hungnhan.school_management.service.SchoolClassService;
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
public class SchoolClassServiceImpl implements SchoolClassService {

    private final SchoolClassRepository schoolClassRepository;
    private final MajorRepository majorRepository;
    private final AcademicYearRepository academicYearRepository;
    private final TeacherRepository teacherRepository;
    private final SchoolClassMapper schoolClassMapper;

    @Override
    public SchoolClassResponse createClass(SchoolClassRequest request) {
        if (schoolClassRepository.existsByCode(request.getCode())) {
            throw new AppException(ErrorCode.CLASS_EXISTED);
        }

        Major major = majorRepository.findById(request.getMajorId())
                .orElseThrow(() -> new AppException(ErrorCode.MAJOR_NOT_FOUND));

        AcademicYear entryAcademicYear = null;
        if (request.getEntryAcademicYearId() != null) {
            entryAcademicYear = academicYearRepository.findById(request.getEntryAcademicYearId())
                    .orElseThrow(() -> new AppException(ErrorCode.ACADEMIC_YEAR_NOT_FOUND));
        }

        Teacher homeroomTeacher = null;
        if (request.getHomeroomTeacherId() != null) {
            homeroomTeacher = teacherRepository.findById(request.getHomeroomTeacherId())
                    .orElseThrow(() -> new AppException(ErrorCode.TEACHER_NOT_FOUND));
        }

        SchoolClass schoolClass = schoolClassMapper.toSchoolClass(request);
        schoolClass.setMajor(major);
        schoolClass.setEntryAcademicYear(entryAcademicYear);
        schoolClass.setHomeroomTeacher(homeroomTeacher);

        if (request.getStatus() != null) {
            schoolClass.setStatus(ClassStatus.valueOf(request.getStatus()));
        }

        return schoolClassMapper.toSchoolClassResponse(schoolClassRepository.save(schoolClass));
    }

    @Override
    public SchoolClassResponse updateClass(Long id, SchoolClassRequest request) {
        SchoolClass schoolClass = schoolClassRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.CLASS_NOT_FOUND));

        if (!schoolClass.getCode().equals(request.getCode()) && schoolClassRepository.existsByCode(request.getCode())) {
            throw new AppException(ErrorCode.CLASS_EXISTED);
        }

        Major major = majorRepository.findById(request.getMajorId())
                .orElseThrow(() -> new AppException(ErrorCode.MAJOR_NOT_FOUND));

        AcademicYear entryAcademicYear = null;
        if (request.getEntryAcademicYearId() != null) {
            entryAcademicYear = academicYearRepository.findById(request.getEntryAcademicYearId())
                    .orElseThrow(() -> new AppException(ErrorCode.ACADEMIC_YEAR_NOT_FOUND));
        }

        Teacher homeroomTeacher = null;
        if (request.getHomeroomTeacherId() != null) {
            homeroomTeacher = teacherRepository.findById(request.getHomeroomTeacherId())
                    .orElseThrow(() -> new AppException(ErrorCode.TEACHER_NOT_FOUND));
        }

        schoolClassMapper.updateSchoolClass(schoolClass, request);
        schoolClass.setMajor(major);
        schoolClass.setEntryAcademicYear(entryAcademicYear);
        schoolClass.setHomeroomTeacher(homeroomTeacher);

        if (request.getStatus() != null) {
            schoolClass.setStatus(ClassStatus.valueOf(request.getStatus()));
        }

        return schoolClassMapper.toSchoolClassResponse(schoolClassRepository.save(schoolClass));
    }

    @Override
    public PageResponse<SchoolClassResponse> getClasses(String search, Long majorId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<SchoolClass> classPage = schoolClassRepository.searchClasses(search, majorId, pageable);

        List<SchoolClassResponse> content = classPage.getContent().stream()
                .map(schoolClassMapper::toSchoolClassResponse)
                .collect(Collectors.toList());

        return PageResponse.<SchoolClassResponse>builder()
                .content(content)
                .pageNumber(classPage.getNumber())
                .pageSize(classPage.getSize())
                .totalElements(classPage.getTotalElements())
                .totalPages(classPage.getTotalPages())
                .last(classPage.isLast())
                .build();
    }

    @Override
    public SchoolClassResponse getClassById(Long id) {
        SchoolClass schoolClass = schoolClassRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.CLASS_NOT_FOUND));
        return schoolClassMapper.toSchoolClassResponse(schoolClass);
    }

    @Override
    public void deleteClass(Long id) {
        SchoolClass schoolClass = schoolClassRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.CLASS_NOT_FOUND));
        schoolClassRepository.delete(schoolClass);
    }
}
