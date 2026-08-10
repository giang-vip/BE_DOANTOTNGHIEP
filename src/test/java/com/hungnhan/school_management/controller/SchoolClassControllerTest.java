package com.hungnhan.school_management.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hungnhan.school_management.dto.request.SchoolClassRequest;
import com.hungnhan.school_management.dto.response.SchoolClassResponse;
import com.hungnhan.school_management.service.SchoolClassService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class SchoolClassControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private SchoolClassService schoolClassService;

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void create_ReturnsOk() throws Exception {
        SchoolClassRequest request = new SchoolClassRequest();
        request.setCode("D20CQCN01-N");
        request.setName("Công nghệ thông tin 01");
        request.setMajorId(1L);
        request.setHomeroomTeacherId(1L);

        SchoolClassResponse response = new SchoolClassResponse();
        response.setCode("D20CQCN01-N");

        Mockito.when(schoolClassService.createClass(any())).thenReturn(response);

        mockMvc.perform(post("/api/admin/classes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }
}
