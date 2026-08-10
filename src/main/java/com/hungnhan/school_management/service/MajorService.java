package com.hungnhan.school_management.service;

import com.hungnhan.school_management.dto.request.MajorRequest;
import com.hungnhan.school_management.dto.response.MajorResponse;
import java.util.List;

import com.hungnhan.school_management.dto.response.PageResponse;

public interface MajorService {
    MajorResponse createMajor(MajorRequest request);
    MajorResponse updateMajor(Long id, MajorRequest request);
    MajorResponse getMajorById(Long id);
    PageResponse<MajorResponse> getAllMajors(String search, Long departmentId, int page, int size);
    void deleteMajor(Long id);
}
