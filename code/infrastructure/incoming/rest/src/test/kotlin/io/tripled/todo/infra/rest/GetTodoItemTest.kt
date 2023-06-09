package io.tripled.todo.infra.rest

import io.tripled.todo.TodoId
import io.tripled.todo.TodoItemStatus
import io.tripled.todo.UserId
import io.tripled.todo.command.CreateTodoItem
import io.tripled.todo.infra.rest.testing.RestTest
import io.tripled.todo.query.GetTodoItem
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.skyscreamer.jsonassert.JSONAssert
import org.skyscreamer.jsonassert.JSONAssert.assertEquals
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class GetTodoItemTest : RestTest() {

	@Test
	fun `getting a todo item`() {
		fakeApp.responseFor(GetTodoItem::class,
			GetTodoItem.Response(GetTodoItem.Response.TodoItem(
				TodoId.existing("todo-123"),
				"paint the room",
				"paint the room in a series of pastel colors",
				UserId.existing("vincent"),
				TodoItemStatus.CREATED,
			)))

		val result = this.mockMvc
			.perform(
				get("/api/todo/todo-123")
					.contentType(MediaType.APPLICATION_JSON_VALUE)
			)
			.andDo(print())
			.andExpect(status().isOk)
			.andReturn()

		val content: String = result.response.contentAsString
		assertEquals(
			content, """{
						"id" : "todo-123",
						"title" : "paint the room",
						"description" : "paint the room in a series of pastel colors",
						"assignee" : "vincent",
						"status" : "CREATED"
					}""", false
		)

	}
}
