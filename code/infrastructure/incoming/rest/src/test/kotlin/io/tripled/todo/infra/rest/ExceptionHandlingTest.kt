package io.tripled.todo.infra.rest

import io.tripled.todo.ValidationException
import io.tripled.todo.infra.rest.testing.RestTest
import org.junit.jupiter.api.Test
import org.springframework.context.annotation.Import
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import org.skyscreamer.jsonassert.JSONAssert.assertEquals


@Import(value = [ExceptionHandlingTest.ErrorsRestTestController::class])
class ExceptionHandlingTest : RestTest() {

	@Test
	fun `when receiving a validation error we get a bad request`() {
		val result = this.mockMvc
			.perform(
				get("/test/api/validationError")
			)
			.andDo(print())
			.andExpect(status().isBadRequest)
			.andReturn()

		val content: String = result.getResponse().contentAsString
		assertEquals(content, """[ 
				{
				  "field" : "testField1",
				  "message" : "something wrong"
				}, {
				  "field" : "testField2",
				  "message" : "something else wrong"
				} 
				]""", false)

	}

	@RestController
	class ErrorsRestTestController {
		@GetMapping("/test/api/validationError")
		fun triggerValidationException(){
			throw ValidationException(listOf(
				ValidationException.Validation("testField1", "something wrong"),
				ValidationException.Validation("testField2", "something else wrong"),
			))
		}
	}
}
