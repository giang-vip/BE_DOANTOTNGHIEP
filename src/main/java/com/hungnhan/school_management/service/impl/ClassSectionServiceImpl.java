package com.hungnhan.school_management.service.impl;

import com.hungnhan.school_management.constant.SectionStatus;
import com.hungnhan.school_management.dto.request.ClassSectionRequest;
import com.hungnhan.school_management.dto.response.ClassSectionResponse;
import com.hungnhan.school_management.dto.response.PageResponse;
import com.hungnhan.school_management.entity.ClassSection;
import com.hungnhan.school_management.entity.SchoolClass;
import com.hungnhan.school_management.entity.Department;
import com.hungnhan.school_management.entity.Major;
import com.hungnhan.school_management.entity.Semester;
import com.hungnhan.school_management.entity.Subject;
import com.hungnhan.school_management.entity.Teacher;
import com.hungnhan.school_management.exception.AppException;
import com.hungnhan.school_management.exception.ErrorCode;
import com.hungnhan.school_management.mapper.ClassSectionMapper;
import com.hungnhan.school_management.repository.ClassSectionRepository;
import com.hungnhan.school_management.repository.DepartmentRepository;
import com.hungnhan.school_management.repository.MajorRepository;
import com.hungnhan.school_management.repository.SchoolClassRepository;
import com.hungnhan.school_management.repository.SemesterRepository;
import com.hungnhan.school_management.repository.SubjectRepository;
import com.hungnhan.school_management.repository.TeacherRepository;
import com.hungnhan.school_management.repository.EnrollmentRepository;
import com.hungnhan.school_management.service.ClassSectionService;
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
public class ClassSectionServiceImpl implements ClassSectionService {

    private final ClassSectionRepository classSectionRepository;
    private final DepartmentRepository departmentRepository;
    private final MajorRepository majorRepository;
    private final SubjectRepository subjectRepository;
    private final TeacherRepository teacherRepository;
    private final SemesterRepository semesterRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final ClassSectionMapper classSectionMapper;

    @Override
    public ClassSectionResponse createClassSection(ClassSectionRequest request) {
        validateTimeAndDate(request);

        if (classSectionRepository.existsBySectionCode(request.getSectionCode())) {
            throw new AppException(ErrorCode.CLASS_SECTION_EXISTED);
        }

        validateConflicts(request, null);

        Department department = null;
        if (request.getDepartmentId() != null) {
            department = departmentRepository.findById(request.getDepartmentId())
                    .orElseThrow(() -> new AppException(ErrorCode.DEPARTMENT_NOT_FOUND));
        }

        Major major = null;
        if (request.getMajorId() != null) {
            major = majorRepository.findById(request.getMajorId())
                    .orElseThrow(() -> new AppException(ErrorCode.MAJOR_NOT_FOUND));
        }

        Subject subject = subjectRepository.findById(request.getSubjectId())
                .orElseThrow(() -> new AppException(ErrorCode.SUBJECT_NOT_FOUND));

        Teacher teacher = teacherRepository.findById(request.getTeacherId())
                .orElseThrow(() -> new AppException(ErrorCode.TEACHER_NOT_FOUND));

        Semester semester = null;
        if (request.getSemesterId() != null) {
            semester = semesterRepository.findById(request.getSemesterId())
                    .orElseThrow(() -> new AppException(ErrorCode.SEMESTER_NOT_FOUND));
        }

        ClassSection classSection = classSectionMapper.toClassSection(request);
        classSection.setDepartment(department);
        classSection.setMajor(major);
        classSection.setSubject(subject);
        classSection.setTeacher(teacher);
        classSection.setSemester(semester);

        if (request.getStatus() != null) {
            classSection.setStatus(SectionStatus.valueOf(request.getStatus()));
        }

        return classSectionMapper.toClassSectionResponse(classSectionRepository.save(classSection));
    }

    @Override
    public ClassSectionResponse updateClassSection(Long id, ClassSectionRequest request) {
        validateTimeAndDate(request);

        ClassSection classSection = classSectionRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.CLASS_SECTION_NOT_FOUND));

        if (!classSection.getSectionCode().equals(request.getSectionCode()) && classSectionRepository.existsBySectionCode(request.getSectionCode())) {
            throw new AppException(ErrorCode.CLASS_SECTION_EXISTED);
        }

        validateConflicts(request, id);

        Department department = null;
        if (request.getDepartmentId() != null) {
            department = departmentRepository.findById(request.getDepartmentId())
                    .orElseThrow(() -> new AppException(ErrorCode.DEPARTMENT_NOT_FOUND));
        }

        Major major = null;
        if (request.getMajorId() != null) {
            major = majorRepository.findById(request.getMajorId())
                    .orElseThrow(() -> new AppException(ErrorCode.MAJOR_NOT_FOUND));
        }

        Subject subject = subjectRepository.findById(request.getSubjectId())
                .orElseThrow(() -> new AppException(ErrorCode.SUBJECT_NOT_FOUND));

        Teacher teacher = teacherRepository.findById(request.getTeacherId())
                .orElseThrow(() -> new AppException(ErrorCode.TEACHER_NOT_FOUND));

        Semester semester = null;
        if (request.getSemesterId() != null) {
            semester = semesterRepository.findById(request.getSemesterId())
                    .orElseThrow(() -> new AppException(ErrorCode.SEMESTER_NOT_FOUND));
        }

        classSectionMapper.updateClassSection(classSection, request);
        classSection.setDepartment(department);
        classSection.setMajor(major);
        classSection.setSubject(subject);
        classSection.setTeacher(teacher);
        classSection.setSemester(semester);

        if (request.getStatus() != null) {
            classSection.setStatus(SectionStatus.valueOf(request.getStatus()));
        }

        return classSectionMapper.toClassSectionResponse(classSectionRepository.save(classSection));
    }

    @Override
    public PageResponse<ClassSectionResponse> getClassSections(String search, Long semesterId, Long subjectId, Long departmentId, Long majorId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ClassSection> classSectionPage = classSectionRepository.searchClassSections(search, semesterId, subjectId, departmentId, majorId, pageable);

        List<Long> classSectionIds = classSectionPage.getContent().stream()
                .map(ClassSection::getId)
                .collect(Collectors.toList());
                
        java.util.Map<Long, Integer> enrollmentCounts = new java.util.HashMap<>();
        if (!classSectionIds.isEmpty()) {
            List<Object[]> counts = enrollmentRepository.countActiveEnrollmentsByClassSectionIds(classSectionIds);
            for (Object[] row : counts) {
                Long id = (Long) row[0];
                Long count = (Long) row[1];
                enrollmentCounts.put(id, count.intValue());
            }
        }

        List<ClassSectionResponse> content = classSectionPage.getContent().stream()
                .map(cs -> {
                    ClassSectionResponse res = classSectionMapper.toClassSectionResponse(cs);
                    res.setEnrolledCount(enrollmentCounts.getOrDefault(cs.getId(), 0));
                    return res;
                })
                .collect(Collectors.toList());

        return PageResponse.<ClassSectionResponse>builder()
                .content(content)
                .pageNumber(classSectionPage.getNumber())
                .pageSize(classSectionPage.getSize())
                .totalElements(classSectionPage.getTotalElements())
                .totalPages(classSectionPage.getTotalPages())
                .last(classSectionPage.isLast())
                .build();
    }

    @Override
    public ClassSectionResponse getClassSectionById(Long id) {
        ClassSection classSection = classSectionRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.CLASS_SECTION_NOT_FOUND));
        ClassSectionResponse res = classSectionMapper.toClassSectionResponse(classSection);
        res.setEnrolledCount((int) enrollmentRepository.countActiveEnrollmentsByClassSectionId(id));
        return res;
    }

    @Override
    public void deleteClassSection(Long id) {
        ClassSection classSection = classSectionRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.CLASS_SECTION_NOT_FOUND));
        classSectionRepository.delete(classSection);
    }

    private void validateTimeAndDate(ClassSectionRequest request) {
        if (request.getStartTime().isAfter(request.getEndTime()) || request.getStartTime().equals(request.getEndTime())) {
            throw new AppException(ErrorCode.INVALID_TIME);
        }
        if (request.getStartDate().isAfter(request.getEndDate()) || request.getStartDate().isEqual(request.getEndDate())) {
            throw new AppException(ErrorCode.INVALID_DATE);
        }
    }

    private void validateConflicts(ClassSectionRequest request, Long excludeId) {
        if (request.getRoom() != null && !request.getRoom().trim().isEmpty()) {
            boolean roomConflict = classSectionRepository.checkRoomConflict(
                    request.getRoom(), request.getWeekday(), request.getStartDate(), request.getEndDate(),
                    request.getStartTime(), request.getEndTime(), excludeId
            );
            if (roomConflict) {
                throw new AppException(ErrorCode.ROOM_SCHEDULE_CONFLICT);
            }
        }

        boolean teacherConflict = classSectionRepository.checkTeacherConflict(
                request.getTeacherId(), request.getWeekday(), request.getStartDate(), request.getEndDate(),
                request.getStartTime(), request.getEndTime(), excludeId
        );
        if (teacherConflict) {
            throw new AppException(ErrorCode.TEACHER_SCHEDULE_CONFLICT);
        }
    }
}
