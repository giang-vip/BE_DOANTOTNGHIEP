package com.hungnhan.school_management.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hungnhan.school_management.dto.request.DepartmentRequest;
import com.hungnhan.school_management.dto.response.DepartmentResponse;
import com.hungnhan.school_management.dto.response.PageResponse;
import com.hungnhan.school_management.service.DepartmentService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class DepartmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private DepartmentService departmentService;

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void createDepartment_ValidRequest_ReturnsOk() throws Exception {
        DepartmentRequest request = new DepartmentRequest();
        request.setCode("CNTT");
        request.setName("Công nghệ thông tin");

        DepartmentResponse mockResponse = new DepartmentResponse();
        mockResponse.setCode("CNTT");

        Mockito.when(departmentService.createDepartment(any(DepartmentRequest.class))).thenReturn(mockResponse);

        mockMvc.perform(post("/api/admin/departments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(1000))
                .andExpect(jsonPath("$.result.code").value("CNTT"));
    }

    @Test
    @WithMockUser(username = "student", roles = {"STUDENT"})
    void createDepartment_NotAdmin_ReturnsForbidden() throws Exception {
        DepartmentRequest request = new DepartmentRequest();
        request.setCode("CNTT");
        request.setName("Công nghệ thông tin");

        mockMvc.perform(post("/api/admin/departments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void getDepartments_ReturnsOk() throws Exception {
        PageResponse<DepartmentResponse> mockPage = new PageResponse<>();
        mockPage.setContent(List.of(new DepartmentResponse()));
        mockPage.setTotalElements(1);

        Mockito.when(departmentService.getDepartments(any(), eq(0), eq(10))).thenReturn(mockPage);

        mockMvc.perform(get("/api/admin/departments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(1000))
                .andExpect(jsonPath("$.result.totalElements").value(1));
    }
}
