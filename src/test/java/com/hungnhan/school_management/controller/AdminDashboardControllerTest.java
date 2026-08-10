package com.hungnhan.school_management.controller;

import com.hungnhan.school_management.dto.response.AdminDashboardResponse;
import com.hungnhan.school_management.service.AdminDashboardService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class AdminDashboardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AdminDashboardService dashboardService;

    @Test
    @WithMockUser(roles = "ADMIN")
    void getStats_Success() throws Exception {
        AdminDashboardResponse response = AdminDashboardResponse.builder()
                .totalStudents(100)
                .totalTeachers(10)
                .totalClasses(20)
                .attendanceRate(90.5)
                .lowGpaStudentsCount(2)
                .build();

        when(dashboardService.getDashboardStats()).thenReturn(response);

        mockMvc.perform(get("/api/admin/dashboard/stats")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result.totalStudents").value(100))
                .andExpect(jsonPath("$.result.lowGpaStudentsCount").value(2));
    }
}
