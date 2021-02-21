package io.tripled.todo.infra.validation

import io.tripled.todo.TodoId
import io.tripled.todo.UserId
import io.tripled.todo.command.AssignTodoItem
import io.tripled.todo.infra.validation.configuration.ValidationConfiguration
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.fail
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension
import javax.validation.Validator

@ExtendWith(SpringExtension::class)
@SpringBootTest(classes = [ValidationConfiguration::class])
class AssignTodoItemValidatorTest {

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
			assertThat(ve.validations).containsExactlyInAnyOrder(
				ValidationException.Validation("todoId", "must not be blank"),
			)

			assertThat(ve.validations[0].field).isEqualTo("todoId")
			assertThat(ve.validations[0].message).isEqualTo("must not be blank")
		}
	}
	@Test
	fun `a valid request to create a todo gets validated`() {
		// given: a valid request
		val delegatee = Delegatee()
		val request = AssignTodoItem.Request(TodoId.existing("todoId"), UserId.existing("userId"))

		// when
		AssignTodoItemValidator(delegatee, validator).assign(request)

		// then
		assertThat(delegatee.calledWithRequest)
			.isEqualTo(AssignTodoItem.Request(TodoId.existing("todoId"), UserId.existing("userId")))
	}

	class Delegatee : AssignTodoItem {
		lateinit var calledWithRequest: AssignTodoItem.Request
		override fun assign(request: AssignTodoItem.Request){
			calledWithRequest = request
		}
	}
}
