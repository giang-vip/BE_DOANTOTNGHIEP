package com.hungnhan.school_management.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hungnhan.school_management.dto.request.ClassSectionRequest;
import com.hungnhan.school_management.dto.response.ClassSectionResponse;
import com.hungnhan.school_management.service.ClassSectionService;
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
import java.time.LocalTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class ClassSectionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ClassSectionService classSectionService;

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void createClassSection_ReturnsOk() throws Exception {
        ClassSectionRequest request = new ClassSectionRequest();
        request.setClassId(1L);
        request.setSubjectId(1L);
        request.setTeacherId(1L);
        request.setSectionCode("INT1306-01");
        request.setWeekday(2);
        request.setStartTime(LocalTime.of(7, 0));
        request.setEndTime(LocalTime.of(9, 0));
        request.setStartDate(LocalDate.of(2023, 9, 1));
        request.setEndDate(LocalDate.of(2023, 12, 1));
        
        ClassSectionResponse response = new ClassSectionResponse();
        response.setSectionCode("INT1306-01");

        Mockito.when(classSectionService.createClassSection(any(ClassSectionRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/admin/class-sections")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }
}
