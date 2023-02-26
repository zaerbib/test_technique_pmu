package com.test.pmu.service;

import com.test.pmu.entity.Course;
import com.test.pmu.entity.Partant;
import com.test.pmu.exception.PmuException;
import com.test.pmu.repository.CourseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class CourseServiceTestUnit {

    @Mock
    private CourseRepository courseRepository;

    @InjectMocks
    private CourseService courseService;

    private Course error1, error2, error3, ok;

    @BeforeEach
    public void setUp() {
        Partant p1,p2,p3,p4,p5;
        p1 = Partant.builder().nom("p1").number(1).build();
        p2 = Partant.builder().nom("p2").number(2).build();
        p3 = Partant.builder().nom("p3").number(3).build();
        p4 = Partant.builder().nom("p4").number(4).build();
        p5 = Partant.builder().nom("p5").number(5).build();

        error1 = Course.builder().nom("error1").jour(LocalDate.now())
                .nombreUnique(2).partants(new HashSet<>(Arrays.asList(p1, p2, p4))).build();

        error2 =  Course.builder().nom("error2").jour(LocalDate.now())
                .nombreUnique(2).partants(new HashSet<>(Arrays.asList(p3, p4))).build();

        error3 =  Course.builder().nom("error3").jour(LocalDate.now())
                .nombreUnique(2).partants(new HashSet<>(Arrays.asList(p1, p4, p4))).build();

        ok =  Course.builder().nom("ok").jour(LocalDate.now())
                .nombreUnique(2).partants(new HashSet<>(Arrays.asList(p1, p2, p3, p4, p5))).build();
    }

    @DisplayName("Test saveCourse méthod")
    @Test
    public void givenCourse_WhenSaveCourse_ThenReturnCourse() throws PmuException {
        // given course
        given(courseRepository.save(ok)).willReturn(ok);

        // when save course
        Course course = courseService.saveCourse(ok);

        // then return course
        assertThat(course).isNotNull();
    }

    @DisplayName("Test saveCourse throws Exception")
    @Test
    public void whenSaveCourse_ThrowsPmuException() {
        // when save course
        Exception exception1 = assertThrows(PmuException.class, () -> courseService.saveCourse(error1));
        Exception exception2 = assertThrows(PmuException.class, () -> courseService.saveCourse(error2));

        String message1 = "les partants ne sont pas correctement numérotés";
        String message2 = "Aucun partant n'a le numéro 1";

        assertThat(exception1.getMessage()).isEqualTo(message1);
        assertThat(exception2.getMessage()).isEqualTo(message2);
    }
}
