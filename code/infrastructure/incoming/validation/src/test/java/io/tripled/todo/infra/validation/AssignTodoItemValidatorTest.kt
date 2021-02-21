package io.tripled.todo.infra.validation

import io.tripled.todo.TodoId
import io.tripled.todo.command.CreateTodoItem
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.fail
import org.assertj.core.api.Condition
import org.junit.jupiter.api.Test
class AssignTodoItemValidatorTest {

	@Test
	fun `a request to create a todo gets validated`() {
		// given: an invalid request
		val dummy = object : CreateTodoItem {
			override fun create(request: CreateTodoItem.Request): CreateTodoItem.Response {
				throw RuntimeException("Should not be invoked")
			}
		}
		val request = CreateTodoItem.Request("", "")

		// when
		try {
			CreateTodoItemValidator(dummy).create(request)
			fail("Should not pass without validation")
		} catch (ve: ValidationException){
			assertThat(ve).`is`(
				containingTheSameValidations(
					listOf(
						ValidationException.Validation("title", "should not be empty"),
						ValidationException.Validation("description", "should not be empty"),
					)
				)
			)

			assertThat(ve.validations[0].field).isEqualTo("title")
			assertThat(ve.validations[0].message).isEqualTo("should not be empty")
		}
	}

	private fun containingTheSameValidations(validations: List<ValidationException.Validation>): Condition<Throwable>
		= Condition<ValidationException>({ ve -> ve.validations == validations }, "same validations") as Condition<Throwable>

	@Test
	fun `a valid request to create a todo gets validated`() {
		// given: a valid request
		val delegatee = Delegatee(CreateTodoItem.Response(TodoId("delegatee-response")))
		val request = CreateTodoItem.Request("a title", "a description")

		// when
		val response = CreateTodoItemValidator(delegatee).create(request)

		// then
		assertThat(delegatee.calledWithRequest)
			.isEqualTo(CreateTodoItem.Request("a title", "a description"))
		assertThat(response)
			.isEqualTo(CreateTodoItem.Response(TodoId("delegatee-response")))

	}

	class Delegatee(private val response: CreateTodoItem.Response) : CreateTodoItem {
		lateinit var calledWithRequest: CreateTodoItem.Request
		override fun create(request: CreateTodoItem.Request): CreateTodoItem.Response {
			calledWithRequest = request
			return response
		}
	}

}
