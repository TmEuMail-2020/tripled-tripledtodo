package io.tripled.todo

import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(classes = [TasksApplication::class])
class TasksApplicationTests {

	@Test
	fun configuredContextLoads() {
		println("valid noop, does spring magic")
	}

	@Test
	fun applicationStarts() {
		main()
	}

}
