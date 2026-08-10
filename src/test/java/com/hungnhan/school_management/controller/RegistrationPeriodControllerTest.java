package com.hungnhan.school_management.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hungnhan.school_management.dto.request.RegistrationPeriodRequest;
import com.hungnhan.school_management.dto.response.RegistrationPeriodResponse;
import com.hungnhan.school_management.service.RegistrationPeriodService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class RegistrationPeriodControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RegistrationPeriodService service;

    @Autowired
    private ObjectMapper objectMapper;

    private RegistrationPeriodRequest request;
    private RegistrationPeriodResponse response;

    @BeforeEach
    void setUp() {
        request = RegistrationPeriodRequest.builder()
                .semesterId(1L)
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(10))
                .isOpen(true)
                .build();

        response = RegistrationPeriodResponse.builder()
                .id(1L)
                .semesterId(1L)
                .semesterCode("HK1")
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(10))
                .isOpen(true)
                .build();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void createOrUpdate_Success() throws Exception {
        when(service.createOrUpdateRegistrationPeriod(any())).thenReturn(response);

        mockMvc.perform(post("/api/admin/config/registration-period")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result.semesterCode").value("HK1"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getCurrent_Success() throws Exception {
        when(service.getCurrentRegistrationPeriod()).thenReturn(response);

        mockMvc.perform(get("/api/admin/config/registration-period/current"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result.semesterCode").value("HK1"));
    }
}
