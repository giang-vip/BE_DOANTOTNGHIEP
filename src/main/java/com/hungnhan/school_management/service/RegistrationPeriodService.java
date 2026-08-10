package com.hungnhan.school_management.service;

import com.hungnhan.school_management.dto.request.RegistrationPeriodRequest;
import com.hungnhan.school_management.dto.response.RegistrationPeriodResponse;

import java.util.List;

public interface RegistrationPeriodService {
    RegistrationPeriodResponse createOrUpdateRegistrationPeriod(RegistrationPeriodRequest request);
    RegistrationPeriodResponse getCurrentRegistrationPeriod();
    List<RegistrationPeriodResponse> getAllRegistrationPeriods();
    void toggleRegistrationPeriod(Long id, boolean isOpen);
}
