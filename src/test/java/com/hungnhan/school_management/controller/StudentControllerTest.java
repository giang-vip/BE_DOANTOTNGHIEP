package com.hungnhan.school_management.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hungnhan.school_management.dto.request.StudentRequest;
import com.hungnhan.school_management.dto.response.StudentResponse;
import com.hungnhan.school_management.service.StudentService;
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
public class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private StudentService studentService;

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void createStudent_ReturnsOk() throws Exception {
        StudentRequest request = new StudentRequest();
        request.setUserId(1L);
        request.setStudentCode("B20DCCN001");
        request.setFullName("Nguyen Van A");
        request.setMajorId(1L);
        
        StudentResponse response = new StudentResponse();
        response.setStudentCode("B20DCCN001");

        Mockito.when(studentService.createStudent(any(StudentRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/admin/students")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }
}
