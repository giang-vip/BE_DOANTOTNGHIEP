package com.hungnhan.school_management.service.impl;

import com.hungnhan.school_management.dto.request.MajorRequest;
import com.hungnhan.school_management.dto.response.MajorResponse;
import com.hungnhan.school_management.entity.Department;
import com.hungnhan.school_management.entity.Major;
import com.hungnhan.school_management.exception.AppException;
import com.hungnhan.school_management.exception.ErrorCode;
import com.hungnhan.school_management.mapper.MajorMapper;
import com.hungnhan.school_management.repository.DepartmentRepository;
import com.hungnhan.school_management.repository.MajorRepository;
import com.hungnhan.school_management.repository.SubjectRepository;
import com.hungnhan.school_management.service.MajorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import com.hungnhan.school_management.dto.response.PageResponse;

@Service
@RequiredArgsConstructor
@org.springframework.transaction.annotation.Transactional
public class MajorServiceImpl implements MajorService {

    private final MajorRepository majorRepository;
    private final DepartmentRepository departmentRepository;
    private final SubjectRepository subjectRepository;
    private final MajorMapper majorMapper;

    @Override
    @Transactional
    public MajorResponse createMajor(MajorRequest request) {
        if (majorRepository.existsByCode(request.getCode())) {
            throw new RuntimeException("Major code already exists"); 
        }
        
        Department department = departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(() -> new AppException(ErrorCode.DEPARTMENT_NOT_FOUND));

        Major major = majorMapper.toMajor(request);
        major.setDepartment(department);
        
        if (request.getStatus() != null) {
            major.setStatus(Major.MajorStatus.valueOf(request.getStatus().toUpperCase()));
        }

        major = majorRepository.save(major);
        return majorMapper.toMajorResponse(major);
    }

    @Override
    @Transactional
    public MajorResponse updateMajor(Long id, MajorRequest request) {
        Major major = majorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Major not found"));
                
        // Ignore code check for simplicity or check if new code exists
        if (!major.getCode().equals(request.getCode()) && majorRepository.existsByCode(request.getCode())) {
            throw new RuntimeException("Major code already exists");
        }

        Department department = departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(() -> new RuntimeException("Department not found"));

        majorMapper.updateMajorFromRequest(request, major);
        major.setDepartment(department);
        
        if (request.getStatus() != null) {
            major.setStatus(Major.MajorStatus.valueOf(request.getStatus().toUpperCase()));
        }

        major = majorRepository.save(major);
        return majorMapper.toMajorResponse(major);
    }

    @Override
    public MajorResponse getMajorById(Long id) {
        Major major = majorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Major not found"));
        return majorMapper.toMajorResponse(major);
    }

    @Override
    public PageResponse<MajorResponse> getAllMajors(String search, Long departmentId, int page, int size) {
        org.springframework.data.domain.Pageable pageable = org.springframework.data.domain.PageRequest.of(page, size);
        org.springframework.data.domain.Page<Major> majorPage;
        
        if (departmentId != null && (search == null || search.trim().isEmpty())) {
            majorPage = majorRepository.findByDepartmentId(departmentId, pageable);
        } else {
            majorPage = majorRepository.searchMajors(search, departmentId, pageable);
        }

        List<MajorResponse> content = majorPage.getContent().stream()
                .map(major -> {
                    MajorResponse response = majorMapper.toMajorResponse(major);
                    // Manually set subjectCount from repository query for reliability
                    long count = subjectRepository.countByMajorId(major.getId());
                    response.setSubjectCount((int) count);
                    return response;
                })
                .collect(Collectors.toList());

        return PageResponse.<MajorResponse>builder()
                .content(content)
                .pageNumber(majorPage.getNumber())
                .pageSize(majorPage.getSize())
                .totalElements(majorPage.getTotalElements())
                .totalPages(majorPage.getTotalPages())
                .last(majorPage.isLast())
                .build();
    }

    @Override
    @Transactional
    public void deleteMajor(Long id) {
        Major major = majorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Major not found"));
        majorRepository.delete(major);
    }
}
