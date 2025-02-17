package com.mariano.spacecrafts;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = SpacecraftsApplicationTests.class)
@ActiveProfiles("test")
class SpacecraftsApplicationTests {

	@Test
	void contextLoads() {
		Assertions.assertDoesNotThrow(this::doNotThrowException);
	}

	private void doNotThrowException() {
	}
}
