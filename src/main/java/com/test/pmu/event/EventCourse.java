package com.test.pmu.event;

import com.test.pmu.entity.Course;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EventCourse {
    private String id;
    private LocalDate date;
    private Course course;
    private Exception exception;
}
