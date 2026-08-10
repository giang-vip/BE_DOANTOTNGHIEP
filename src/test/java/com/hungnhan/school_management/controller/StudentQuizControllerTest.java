package com.hungnhan.school_management.controller;

import com.hungnhan.school_management.dto.response.PageResponse;
import com.hungnhan.school_management.service.StudentQuizService;
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
public class StudentQuizControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentQuizService studentQuizService;

    @Test
    @WithMockUser(username = "student", roles = {"STUDENT"})
    void getQuizResult_ReturnsOk() throws Exception {
        Mockito.when(studentQuizService.getQuizResult(any(String.class), eq(1L)))
               .thenReturn(new com.hungnhan.school_management.dto.response.QuizResultResponse());
        mockMvc.perform(get("/api/student/assignments/1/quiz-result"))
                .andExpect(status().isOk());
    }
}
