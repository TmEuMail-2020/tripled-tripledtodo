package io.tripled.todo

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(classes = [TasksApplication::class])
class TasksApplicationTests {

	@Test
	fun configuredContextLoads() {
	}

	@Test
	fun applicationStarts() {
		main()
	}

}
