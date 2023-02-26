package com.test.pmu.controller;

import com.test.pmu.entity.Course;
import com.test.pmu.exception.PmuException;
import com.test.pmu.service.CourseService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("course/v1")
@AllArgsConstructor
@Slf4j
public class CourseController {
    private final CourseService courseService;

    @PostMapping("/add")
    public ResponseEntity<?> saveCourse(@RequestBody Course course) {
        try {
            return new ResponseEntity<>(courseService.saveCourse(course), HttpStatus.CREATED);
        } catch (PmuException e) {
            log.error(e.getMessage());
            e.setHttpMessage(HttpStatus.NOT_ACCEPTABLE.toString());
            return new ResponseEntity<>(e, HttpStatus.NOT_ACCEPTABLE);
        }
    }
}
