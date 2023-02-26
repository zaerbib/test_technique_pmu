package com.test.pmu.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.pmu.entity.Course;
import com.test.pmu.entity.Partant;
import com.test.pmu.exception.PmuException;
import com.test.pmu.service.CourseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@WebMvcTest
public class CourseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CourseService courseService;

    @Autowired
    private ObjectMapper objectMapper;

    private Course error, ok;

    @BeforeEach
    public void setUp(){
        Partant p1,p2,p3,p4,p5;
        p1 = Partant.builder().nom("p1").number(1).build();
        p2 = Partant.builder().nom("p2").number(2).build();
        p3 = Partant.builder().nom("p3").number(3).build();
        p4 = Partant.builder().nom("p4").number(4).build();
        p5 = Partant.builder().nom("p5").number(5).build();

        ok = Course.builder().nom("ok").jour(LocalDate.now())
                .nombreUnique(1).partants(new HashSet<>(Arrays.asList(p1, p2, p3, p4, p5))).build();

        error = Course.builder().nom("error").jour(LocalDate.now())
                .nombreUnique(2).partants(new HashSet<>(Arrays.asList(p2, p5))).build();
    }

    @DisplayName("Test course controller")
    @Test
    public void givenCourse_WhenCreateCourse_ThenReturnCourse() throws Exception {
        // given course
        given(courseService.saveCourse(any(Course.class)))
                .willAnswer(invocation -> invocation.getArgument(0));

        ResultActions response = mockMvc.perform(post("/course/v1/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(ok)));

        // then - verify
        response.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nom", is(ok.getNom())))
                .andExpect(jsonPath("$.nombreUnique", is(ok.getNombreUnique())));

    }
}
