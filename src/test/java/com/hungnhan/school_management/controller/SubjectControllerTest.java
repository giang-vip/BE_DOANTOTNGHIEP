package com.hungnhan.school_management.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hungnhan.school_management.dto.request.SubjectRequest;
import com.hungnhan.school_management.dto.response.SubjectResponse;
import com.hungnhan.school_management.service.SubjectService;
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
public class SubjectControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private SubjectService subjectService;

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void createSubject_ReturnsOk() throws Exception {
        SubjectRequest request = new SubjectRequest();
        request.setCode("INT1306");
        request.setName("Cơ sở dữ liệu");
        request.setCredits(3);
        
        SubjectResponse response = new SubjectResponse();
        response.setCode("INT1306");

        Mockito.when(subjectService.createSubject(any(SubjectRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/admin/subjects")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }
}
