package com.hungnhan.school_management.service.impl;

import com.hungnhan.school_management.dto.request.SubjectRequest;
import com.hungnhan.school_management.dto.response.PageResponse;
import com.hungnhan.school_management.dto.response.SubjectResponse;
import com.hungnhan.school_management.entity.Department;
import com.hungnhan.school_management.entity.Subject;
import com.hungnhan.school_management.exception.AppException;
import com.hungnhan.school_management.exception.ErrorCode;
import com.hungnhan.school_management.mapper.SubjectMapper;
import com.hungnhan.school_management.repository.DepartmentRepository;
import com.hungnhan.school_management.repository.MajorRepository;
import com.hungnhan.school_management.repository.SubjectRepository;
import com.hungnhan.school_management.service.SubjectService;
import com.hungnhan.school_management.constant.SubjectType;
import com.hungnhan.school_management.entity.Major;
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
public class SubjectServiceImpl implements SubjectService {

    private final SubjectRepository subjectRepository;
    private final DepartmentRepository departmentRepository;
    private final MajorRepository majorRepository;
    private final com.hungnhan.school_management.repository.MajorSubjectRepository majorSubjectRepository;
    private final SubjectMapper subjectMapper;

    @Override
    public SubjectResponse createSubject(SubjectRequest request) {
        if (subjectRepository.existsByCode(request.getCode())) {
            throw new AppException(ErrorCode.SUBJECT_EXISTED);
        }

        if (request.getCredits() == null || request.getCredits() < 1) {
            throw new AppException(ErrorCode.SUBJECT_INVALID_CREDITS);
        }

        Department department = null;
        if (request.getDepartmentId() != null) {
            department = departmentRepository.findById(request.getDepartmentId())
                    .orElseThrow(() -> new AppException(ErrorCode.DEPARTMENT_NOT_FOUND));
        }

        Subject subject = subjectMapper.toSubject(request);
        subject.setDepartment(department);
        Subject savedSubject = subjectRepository.save(subject);

        return subjectMapper.toSubjectResponse(savedSubject);
    }

    @Override
    public SubjectResponse updateSubject(Long id, SubjectRequest request) {
        Subject subject = subjectRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.SUBJECT_NOT_FOUND));

        if (!subject.getCode().equals(request.getCode()) && subjectRepository.existsByCode(request.getCode())) {
            throw new AppException(ErrorCode.SUBJECT_EXISTED);
        }

        if (request.getCredits() == null || request.getCredits() < 1) {
            throw new AppException(ErrorCode.SUBJECT_INVALID_CREDITS);
        }

        Department department = null;
        if (request.getDepartmentId() != null) {
            department = departmentRepository.findById(request.getDepartmentId())
                    .orElseThrow(() -> new AppException(ErrorCode.DEPARTMENT_NOT_FOUND));
        }

        subjectMapper.updateSubject(subject, request);
        subject.setDepartment(department);

        Subject updatedSubject = subjectRepository.save(subject);

        return subjectMapper.toSubjectResponse(updatedSubject);
    }

    @Override
    public PageResponse<SubjectResponse> getSubjects(String search, Long departmentId, Long majorId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Subject> subjectPage = subjectRepository.searchSubjects(search, departmentId, majorId, pageable);

        List<SubjectResponse> content = subjectPage.getContent().stream()
                .map(subject -> {
                    SubjectResponse response = subjectMapper.toSubjectResponse(subject);
                    if (majorId != null) {
                        majorSubjectRepository.findByMajorIdAndSubjectId(majorId, subject.getId())
                                .ifPresent(ms -> {
                                    response.setMajorId(ms.getMajor().getId());
                                    response.setMajorName(ms.getMajor().getName());
                                    response.setType(ms.getSubjectType().name());
                                    response.setSemesterIndex(ms.getRecommendedSemester());
                                });
                    }
                    return response;
                })
                .collect(Collectors.toList());

        return PageResponse.<SubjectResponse>builder()
                .content(content)
                .pageNumber(subjectPage.getNumber())
                .pageSize(subjectPage.getSize())
                .totalElements(subjectPage.getTotalElements())
                .totalPages(subjectPage.getTotalPages())
                .last(subjectPage.isLast())
                .build();
    }

    @Override
    public SubjectResponse getSubjectById(Long id) {
        Subject subject = subjectRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.SUBJECT_NOT_FOUND));
        return subjectMapper.toSubjectResponse(subject);
    }

    @Override
    public void deleteSubject(Long id) {
        Subject subject = subjectRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.SUBJECT_NOT_FOUND));
        majorSubjectRepository.deleteBySubjectId(id);
        subjectRepository.delete(subject);
    }
}
