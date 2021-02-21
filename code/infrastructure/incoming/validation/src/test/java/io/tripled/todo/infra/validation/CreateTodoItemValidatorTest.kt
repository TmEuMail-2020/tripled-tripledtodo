package io.tripled.todo.infra.validation

import io.tripled.todo.TodoId
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

	class Delegatee(val response: CreateTodoItem.Response) : CreateTodoItem {
		lateinit var calledWithRequest: CreateTodoItem.Request
		override fun create(request: CreateTodoItem.Request): CreateTodoItem.Response {
			calledWithRequest = request
			return response
		}
	}

}
