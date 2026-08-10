package com.hungnhan.school_management.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hungnhan.school_management.dto.response.PageResponse;
import com.hungnhan.school_management.service.TeacherClassService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class TeacherClassControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TeacherClassService teacherClassService;

    @Test
    @WithMockUser(username = "teacher", roles = {"TEACHER"})
    void getTeacherClasses_ReturnsOk() throws Exception {
        PageResponse mockPage = new PageResponse<>();
        Mockito.when(teacherClassService.getTeacherClassSections(any(String.class), any(), any(), eq(0), eq(10))).thenReturn(mockPage);

        mockMvc.perform(get("/api/teacher/classes"))
                .andExpect(status().isOk());
    }
}
