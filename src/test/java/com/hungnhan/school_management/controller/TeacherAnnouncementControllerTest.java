package com.hungnhan.school_management.controller;

import com.hungnhan.school_management.dto.response.PageResponse;
import com.hungnhan.school_management.service.TeacherAnnouncementService;
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
public class TeacherAnnouncementControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TeacherAnnouncementService teacherAnnouncementService;

    @Test
    @WithMockUser(username = "teacher", roles = {"TEACHER"})
    void getAnnouncements_ReturnsOk() throws Exception {
        PageResponse mockPage = new PageResponse<>();
        Mockito.when(teacherAnnouncementService.getAnnouncements(any(String.class), eq(1L), eq(0), eq(10))).thenReturn(mockPage);

        mockMvc.perform(get("/api/teacher/classes/1/announcements"))
                .andExpect(status().isOk());
    }
}
