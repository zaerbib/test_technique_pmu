package com.test.pmu.repository;

import com.test.pmu.entity.Course;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;

public interface CourseRepository extends CrudRepository<Course, Long> {
    @Query("select case when count(c) > 0 then true else false end from Course c where c.jour =:jour and c.nombreUnique =:number")
    public boolean isCourseExist(LocalDate jour, Integer number);
}
