package io.tripled.todo.infra.rest

import io.tripled.todo.TodoId
import io.tripled.todo.TodoItemStatus
import io.tripled.todo.UserId
import io.tripled.todo.command.CreateTodoItem
import io.tripled.todo.infra.rest.testing.RestTest
import io.tripled.todo.query.GetTodoItem
import io.tripled.todo.query.GetTodoItems
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.skyscreamer.jsonassert.JSONAssert
import org.skyscreamer.jsonassert.JSONAssert.assertEquals
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class GetTodoItemsTest : RestTest() {

	@Test
	fun `getting a todo item`() {
		fakeApp.responseFor(
			GetTodoItems::class,
				GetTodoItems.Response(listOf(
					GetTodoItems.Response.TodoItem(
						TodoId.existing("todo-123"),
						"paint the room",
						"paint the room in a series of pastel colors",
						UserId.existing("vincent"),
						TodoItemStatus.CREATED,
					),
					GetTodoItems.Response.TodoItem(
						TodoId.existing("todo-456"),
						"paint the room 2",
						"paint the room in a series of bright colors",
						UserId.existing("mondriaan"),
						TodoItemStatus.FINISHED,
					),
				)))

		val result = this.mockMvc
			.perform(
				get("/api/todo/")
					.contentType(MediaType.APPLICATION_JSON_VALUE)
			)
			.andDo(print())
			.andExpect(status().isOk)
			.andReturn()

		val content: String = result.response.contentAsString
		assertEquals(
			content, """[{
					  "todo_item" : {
						"id" : "todo-123",
						"title" : "paint the room",
						"description" : "paint the room in a series of pastel colors",
						"assignee" : "vincent",
						"status" : "CREATED"
					  }
					},{
					  "todo_item" : {
						"id" : "todo-456",
						"title" : "paint the room",
						"description" : "paint the room in a series of pastel colors",
						"assignee" : "vincent",
						"status" : "CREATED"
					  }
					}]""", false
		)

	}
}
