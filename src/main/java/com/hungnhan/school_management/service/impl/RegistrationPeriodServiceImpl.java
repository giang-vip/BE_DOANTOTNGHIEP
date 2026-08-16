package com.hungnhan.school_management.service.impl;

import com.hungnhan.school_management.dto.request.RegistrationPeriodRequest;
import com.hungnhan.school_management.dto.response.RegistrationPeriodResponse;
import com.hungnhan.school_management.entity.RegistrationPeriod;
import com.hungnhan.school_management.entity.Semester;
import com.hungnhan.school_management.exception.AppException;
import com.hungnhan.school_management.exception.ErrorCode;
import com.hungnhan.school_management.repository.RegistrationPeriodRepository;
import com.hungnhan.school_management.repository.SemesterRepository;
import com.hungnhan.school_management.service.RegistrationPeriodService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class RegistrationPeriodServiceImpl implements RegistrationPeriodService {

    private final RegistrationPeriodRepository registrationPeriodRepository;
    private final SemesterRepository semesterRepository;

    @Override
    @Transactional
    public RegistrationPeriodResponse createOrUpdateRegistrationPeriod(RegistrationPeriodRequest request) {
        Semester semester = semesterRepository.findById(request.getSemesterId())
                .orElseThrow(() -> new AppException(ErrorCode.SEMESTER_NOT_FOUND));

        RegistrationPeriod period = registrationPeriodRepository.findBySemesterId(semester.getId())
                .orElse(RegistrationPeriod.builder().semester(semester).build());

        period.setStartDate(request.getStartDate());
        period.setEndDate(request.getEndDate());
        if (request.getIsOpen() != null) {
            period.setIsOpen(request.getIsOpen());
        }

        period = registrationPeriodRepository.save(period);
        return mapToResponse(period);
    }

    @Override
    public RegistrationPeriodResponse getCurrentRegistrationPeriod() {
        return registrationPeriodRepository.findBySemesterIsCurrentTrue()
                .map(this::mapToResponse)
                .orElse(null);
    }

    @Override
    public List<RegistrationPeriodResponse> getAllRegistrationPeriods() {
        return registrationPeriodRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void toggleRegistrationPeriod(Long id, boolean isOpen) {
        RegistrationPeriod period = registrationPeriodRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Registration period not found"));
        period.setIsOpen(isOpen);
        registrationPeriodRepository.save(period);
    }

    private RegistrationPeriodResponse mapToResponse(RegistrationPeriod p) {
        return RegistrationPeriodResponse.builder()
                .id(p.getId())
                .semesterId(p.getSemester().getId())
                .semesterCode(p.getSemester().getCode())
                .startDate(p.getStartDate())
                .endDate(p.getEndDate())
                .isOpen(p.getIsOpen())
                .build();
    }

    @Override
    @Transactional
    public void deleteRegistrationPeriod(Long id) {
        if (!registrationPeriodRepository.existsById(id)) {
            throw new RuntimeException("Registration period not found");
        }
        registrationPeriodRepository.deleteById(id);
    }
}
