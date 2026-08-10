package com.hungnhan.school_management.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hungnhan.school_management.dto.request.AcademicYearRequest;
import com.hungnhan.school_management.dto.response.AcademicYearResponse;
import com.hungnhan.school_management.service.AcademicYearService;
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

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class AcademicYearControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AcademicYearService academicYearService;

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void create_ReturnsOk() throws Exception {
        AcademicYearRequest request = new AcademicYearRequest();
        request.setCode("K2023");
        request.setStartDate(LocalDate.of(2023, 9, 1));
        request.setEndDate(LocalDate.of(2024, 6, 30));

        AcademicYearResponse response = new AcademicYearResponse();
        response.setCode("K2023");

        Mockito.when(academicYearService.createAcademicYear(any())).thenReturn(response);

        mockMvc.perform(post("/api/admin/academic-years")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }
}
