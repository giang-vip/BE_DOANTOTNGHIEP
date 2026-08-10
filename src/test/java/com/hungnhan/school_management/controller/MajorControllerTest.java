package com.hungnhan.school_management.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hungnhan.school_management.dto.request.MajorRequest;
import com.hungnhan.school_management.dto.response.MajorResponse;
import com.hungnhan.school_management.service.MajorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class MajorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MajorService majorService;

    @Autowired
    private ObjectMapper objectMapper;

    private MajorRequest request;
    private MajorResponse response;

    @BeforeEach
    void setUp() {
        request = MajorRequest.builder()
                .departmentId(1L)
                .code("CNTT")
                .name("Công nghệ thông tin")
                .status("ACTIVE")
                .build();

        response = MajorResponse.builder()
                .id(1L)
                .departmentId(1L)
                .departmentName("Khoa CNTT")
                .code("CNTT")
                .name("Công nghệ thông tin")
                .status("ACTIVE")
                .build();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void createMajor_Success() throws Exception {
        when(majorService.createMajor(any(MajorRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/admin/majors")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result.code").value("CNTT"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getAllMajors_Success() throws Exception {
        when(majorService.getAllMajors()).thenReturn(List.of(response));

        mockMvc.perform(get("/api/admin/majors")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result[0].code").value("CNTT"));
    }
}
