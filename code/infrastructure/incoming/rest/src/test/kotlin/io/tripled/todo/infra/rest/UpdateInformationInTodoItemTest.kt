package io.tripled.todo.infra.rest

import io.tripled.todo.TodoId
import io.tripled.todo.command.CreateTodoItem
import io.tripled.todo.command.UpdateInformationInTodoItem
import io.tripled.todo.infra.rest.testing.RestTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class UpdateInformationInTodoItemTest : RestTest() {

	@Test
	fun `updating an existing todo item tells us it's updated`() {
		fakeApp.responseFor(CreateTodoItem::class,
							CreateTodoItem.Response(TodoId.existing("generated-todo-id")))

		this.mockMvc
				.perform(put("/api/todo/abc-123/information")
						.contentType(MediaType.APPLICATION_JSON_VALUE)
						.content("""
    					{
							"title": "prep talk",
							"description": "prepare a kotlin talk"
    					}
					"""))
				.andDo(print())
			.andExpect(status().isOk)

		assertThat(fakeApp.requestFrom(UpdateInformationInTodoItem::class)).isEqualTo(
			UpdateInformationInTodoItem.Request(
				TodoId.existing("abc-123"),
				"prep talk",
				"prepare a kotlin talk"
			)
		)
	}
}
