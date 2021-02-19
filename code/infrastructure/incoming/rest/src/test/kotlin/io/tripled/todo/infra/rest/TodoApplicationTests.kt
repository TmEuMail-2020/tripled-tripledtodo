package io.tripled.todo.infra.rest

import io.tripled.todo.TodoId
import io.tripled.todo.command.CancelTodoItem
import io.tripled.todo.command.CreateTodoItem
import io.tripled.todo.infra.rest.testing.RestTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class TodoApplicationTests : RestTest() {

	@Test
	fun `posting a new Todo item tells us it's created`() {
		fakeApp.responseFor(CreateTodoItem::class,
							CreateTodoItem.Response(TodoId.existing("generated-todo-id")))

		this.mockMvc
				.perform(post("/api/todo")
						.contentType(MediaType.APPLICATION_JSON_VALUE)
						.content("""
    					{
							"title": "prep talk",
							"description": "prepare a kotlin talk"
    					}
					"""))
				.andDo(print())
			.andExpect(content().string("{\"id\":\"generated-todo-id\"}"))
			.andExpect(status().isCreated)

		assertThat(fakeApp.requestFrom(CreateTodoItem::class)).isEqualTo(
			CreateTodoItem.Request(
				"prep talk",
				"prepare a kotlin talk"
			)
		)
	}

	@Test
	fun `cancelling a todo item`() {
		this.mockMvc
			.perform(put("/api/todo/todo-123/status")
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content("""
    					{
							"value": "cancel"
    					}
					"""))
			.andDo(print())
			.andExpect(status().isOk)

		assertThat(fakeApp.requestFrom(CancelTodoItem::class)).isEqualTo(
			CancelTodoItem.Request(
				TodoId.existing("todo-123")
			)
		)
	}


	@Test
	fun `sending an invalid status to status fails`() {
		this.mockMvc
			.perform(put("/api/todo/todo-123/status")
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content("""
    					{
							"value": "not-a-valid-status"
    					}
					"""))
			.andDo(print())
			.andExpect(status().isBadRequest)
	}

}
