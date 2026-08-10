package com.hungnhan.school_management.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hungnhan.school_management.dto.request.AdminAnnouncementRequest;
import com.hungnhan.school_management.dto.response.AdminAnnouncementResponse;
import com.hungnhan.school_management.service.AdminAnnouncementService;
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
public class AdminAnnouncementControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AdminAnnouncementService announcementService;

    @Autowired
    private ObjectMapper objectMapper;

    private AdminAnnouncementRequest request;
    private AdminAnnouncementResponse response;

    @BeforeEach
    void setUp() {
        request = AdminAnnouncementRequest.builder()
                .title("Test Title")
                .content("Test Content")
                .recipientGroup("all")
                .build();

        response = AdminAnnouncementResponse.builder()
                .id(1L)
                .title("Test Title")
                .content("Test Content")
                .recipientGroup("all")
                .sender("Admin")
                .build();
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void createAnnouncement_Success() throws Exception {
        when(announcementService.createAnnouncement(any(AdminAnnouncementRequest.class), eq("admin")))
                .thenReturn(response);

        mockMvc.perform(post("/api/admin/announcements")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result.title").value("Test Title"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getAllAnnouncements_Success() throws Exception {
        when(announcementService.getAllAnnouncements()).thenReturn(List.of(response));

        mockMvc.perform(get("/api/admin/announcements")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result[0].title").value("Test Title"));
    }
}
