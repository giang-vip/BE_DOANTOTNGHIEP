package com.hungnhan.school_management.service.impl;

import com.hungnhan.school_management.dto.request.AcademicYearRequest;
import com.hungnhan.school_management.dto.response.AcademicYearResponse;
import com.hungnhan.school_management.dto.response.PageResponse;
import com.hungnhan.school_management.entity.AcademicYear;
import com.hungnhan.school_management.exception.AppException;
import com.hungnhan.school_management.exception.ErrorCode;
import com.hungnhan.school_management.mapper.AcademicYearMapper;
import com.hungnhan.school_management.repository.AcademicYearRepository;
import com.hungnhan.school_management.service.AcademicYearService;
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
public class AcademicYearServiceImpl implements AcademicYearService {

    private final AcademicYearRepository academicYearRepository;
    private final AcademicYearMapper academicYearMapper;

    @Override
    public AcademicYearResponse createAcademicYear(AcademicYearRequest request) {
        if (request.getStartDate().isAfter(request.getEndDate()) || request.getStartDate().isEqual(request.getEndDate())) {
            throw new AppException(ErrorCode.ACADEMIC_YEAR_INVALID_DATES);
        }

        if (academicYearRepository.existsByCode(request.getCode())) {
            throw new AppException(ErrorCode.ACADEMIC_YEAR_EXISTED);
        }

        AcademicYear academicYear = academicYearMapper.toAcademicYear(request);
        if (academicYear.getIsCurrent() == null) {
            academicYear.setIsCurrent(false);
        }
        return academicYearMapper.toAcademicYearResponse(academicYearRepository.save(academicYear));
    }

    @Override
    public AcademicYearResponse updateAcademicYear(Long id, AcademicYearRequest request) {
        if (request.getStartDate().isAfter(request.getEndDate()) || request.getStartDate().isEqual(request.getEndDate())) {
            throw new AppException(ErrorCode.ACADEMIC_YEAR_INVALID_DATES);
        }

        AcademicYear academicYear = academicYearRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.ACADEMIC_YEAR_NOT_FOUND));

        if (!academicYear.getCode().equals(request.getCode()) && academicYearRepository.existsByCode(request.getCode())) {
            throw new AppException(ErrorCode.ACADEMIC_YEAR_EXISTED);
        }

        academicYearMapper.updateAcademicYear(academicYear, request);
        if (academicYear.getIsCurrent() == null) {
            academicYear.setIsCurrent(false);
        }
        
        return academicYearMapper.toAcademicYearResponse(academicYearRepository.save(academicYear));
    }

    @Override
    public PageResponse<AcademicYearResponse> getAcademicYears(String search, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<AcademicYear> academicYearPage = academicYearRepository.searchAcademicYears(search, pageable);

        List<AcademicYearResponse> content = academicYearPage.getContent().stream()
                .map(academicYearMapper::toAcademicYearResponse)
                .collect(Collectors.toList());

        return PageResponse.<AcademicYearResponse>builder()
                .content(content)
                .pageNumber(academicYearPage.getNumber())
                .pageSize(academicYearPage.getSize())
                .totalElements(academicYearPage.getTotalElements())
                .totalPages(academicYearPage.getTotalPages())
                .last(academicYearPage.isLast())
                .build();
    }

    @Override
    public AcademicYearResponse getAcademicYearById(Long id) {
        AcademicYear academicYear = academicYearRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.ACADEMIC_YEAR_NOT_FOUND));
        return academicYearMapper.toAcademicYearResponse(academicYear);
    }

    @Override
    public void deleteAcademicYear(Long id) {
        AcademicYear academicYear = academicYearRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.ACADEMIC_YEAR_NOT_FOUND));
        try {
            academicYearRepository.delete(academicYear);
            academicYearRepository.flush();
        } catch (DataIntegrityViolationException ex) {
            log.error("Lỗi xóa năm học, đang có dữ liệu liên kết: ", ex);
            throw new AppException(ErrorCode.ACADEMIC_YEAR_HAS_REFERENCES);
        }
    }
}
