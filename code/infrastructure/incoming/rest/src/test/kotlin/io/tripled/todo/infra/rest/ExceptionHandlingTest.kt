package io.tripled.todo.infra.rest

import io.tripled.todo.DomainException
import io.tripled.todo.ValidationException
import io.tripled.todo.infra.rest.testing.RestTest
import org.junit.jupiter.api.Test
import org.skyscreamer.jsonassert.JSONAssert.assertEquals
import org.springframework.context.annotation.Import
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController


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

	@Test
	fun `when receiving a domain exception we get a bad request`() {
		val result = this.mockMvc
			.perform(
				get("/test/api/domainError")
			)
			.andDo(print())
			.andExpect(
				MockMvcResultMatchers.content().string("""{
  "message" : "invariant violated"
}"""))
			.andExpect(status().isBadRequest)
			.andReturn()
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
		@GetMapping("/test/api/domainError")
		fun triggerDomainException(){
			throw DomainException("invariant violated")
		}
	}
}
