package com.hungnhan.school_management.service;

import com.hungnhan.school_management.dto.response.AdminDashboardResponse;

import java.util.Map;

public interface AdminDashboardService {
    AdminDashboardResponse getDashboardStats();
    
    Map<String, Integer> getGradeDistribution(Long yearId, Long semesterId, Long classSectionId, Long departmentId, Long majorId);
}
