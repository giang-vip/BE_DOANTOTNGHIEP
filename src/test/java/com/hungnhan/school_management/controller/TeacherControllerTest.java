package com.hungnhan.school_management.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hungnhan.school_management.dto.request.TeacherRequest;
import com.hungnhan.school_management.dto.response.TeacherResponse;
import com.hungnhan.school_management.service.TeacherService;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class TeacherControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TeacherService teacherService;

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void createTeacher_ReturnsOk() throws Exception {
        TeacherRequest request = new TeacherRequest();
        request.setUserId(1L);
        request.setTeacherCode("GV001");
        request.setFullName("Tran Van B");
        request.setDepartmentId(1L);
        
        TeacherResponse response = new TeacherResponse();
        response.setTeacherCode("GV001");

        Mockito.when(teacherService.createTeacher(any(TeacherRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/admin/teachers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }
}
