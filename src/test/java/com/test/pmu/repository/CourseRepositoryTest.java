package com.test.pmu.repository;

import com.test.pmu.entity.Course;
import com.test.pmu.entity.Partant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;

@DataJpaTest
public class CourseRepositoryTest {
    @Autowired
    TestEntityManager entityManager;

    @Autowired
    CourseRepository courseRepository;

    private Course course1, course2, course3, course4;

    @BeforeEach
    public void setUp() {
        Partant p1,p2,p3,p4,p5;
        p1 = Partant.builder().nom("p1").number(1).build();
        p2 = Partant.builder().nom("p2").number(2).build();
        p3 = Partant.builder().nom("p3").number(3).build();
        p4 = Partant.builder().nom("p4").number(4).build();
        p5 = Partant.builder().nom("p5").number(5).build();

        course1 = Course.builder().nom("course1").jour(LocalDate.now())
                .nombreUnique(1).partants(new HashSet<>(Arrays.asList(p1, p2, p4))).build();

        course2 =  Course.builder().nom("course2").jour(LocalDate.now())
                .nombreUnique(2).partants(new HashSet<>(Arrays.asList(p1, p2, p3, p4))).build();

        course3 =  Course.builder().nom("course3").jour(LocalDate.now())
                .nombreUnique(3).partants(new HashSet<>(Arrays.asList(p1, p4, p4))).build();

        course4 =  Course.builder().nom("course4").jour(LocalDate.now())
                .nombreUnique(4).partants(new HashSet<>(Arrays.asList(p1, p2, p3, p4, p5))).build();
    }

    @Test
    public void shouldFindAllCourses() {
        entityManager.persist(course1);
        entityManager.persist(course2);
        entityManager.persist(course3);

        Iterable courses = courseRepository.findAll();

        assertThat(courses).hasSize(3).contains(course1, course2, course3);
    }

    @Test
    public void shouldCourseExist() {
        entityManager.persist(course4);
        boolean tmp = courseRepository.isCourseExist(course4.getJour(), course4.getNombreUnique());
        assertThat(tmp).isTrue();
    }
}
