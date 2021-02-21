package io.tripled.todo.infra.validation

import io.tripled.todo.TodoId
import io.tripled.todo.UserId
import io.tripled.todo.command.AssignTodoItem
import io.tripled.todo.command.CreateTodoItem
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.fail
import org.assertj.core.api.Condition
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Configuration
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.validation.Validator

@ExtendWith(SpringExtension::class)
@SpringBootTest(classes = [CreateTodoItemValidatorTest.DummyConfiguration::class])
class CreateTodoItemValidatorTest {

	@Configuration
	class DummyConfiguration

	@Autowired
	lateinit var validator: Validator

	@Test
	fun `a request to create a todo gets validated`() {
		// given: an invalid request
		val dummy = object : AssignTodoItem {
			override fun assign(request: AssignTodoItem.Request) {
				throw RuntimeException("Should not be invoked")
			}
		}
		val request = AssignTodoItem.Request(TodoId.existing(""), UserId.existing(""))

		// when
		try {
			AssignTodoItemValidator(dummy, validator).assign(request)
			fail("Should not pass without validation")
		} catch (ve: ValidationException){
			assertThat(ve).`is`(
				containingTheSameValidations(
					listOf(
						ValidationException.Validation("userId", "should not be empty"),
					)
				)
			)

			assertThat(ve.validations[0].field).isEqualTo("title")
			assertThat(ve.validations[0].message).isEqualTo("should not be empty")
		}
	}

	private fun containingTheSameValidations(validations: List<ValidationException.Validation>): Condition<Throwable>
		= Condition<ValidationException>({ ve -> ve.validations == validations }, "same validations") as Condition<Throwable>
}
