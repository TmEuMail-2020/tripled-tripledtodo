package io.tripled.todo.infra.validation

import io.tripled.todo.command.CreateTodoItem
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.fail
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
//@SpringBootTest
class CreateTodoItemValidatorTest {

	@Test
	fun `a request to create a todo gets validated`() {
		// given: an invalid request
		val request = CreateTodoItem.Request("", "")

		// when
		try {
			CreateTodoItemValidator().create(request)
			fail("Should not pass without validation")
		} catch (ve: ValidationException){
			assertThat(ve).isEqualTo(
				ValidationException(
					ValidationException.Validation("title", "should not be empty"),
					ValidationException.Validation("description", "should not be empty"),
				)
			)
		}
	}

}
