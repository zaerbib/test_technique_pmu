package com.test.pmu;

import com.test.pmu.service.CourseService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Disabled
class TestApplicationTests {
	@Autowired
	private CourseService courseService;

	@Test
	void contextLoads() {
	}
}
