package io.tripled.todo.infra.rest

import io.tripled.todo.TodoId
import io.tripled.todo.UserId
import io.tripled.todo.command.AssignTodoItem
import io.tripled.todo.command.CreateTodoItem
import io.tripled.todo.command.UpdateInformationInTodoItem
import io.tripled.todo.infra.rest.testing.RestTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class AssignTodoItemTest : RestTest() {

	@Test
	fun `assigning a valid user to a todoitem returns ok`() {
		this.mockMvc
				.perform(put("/api/todo/abc-123/assignee")
						.contentType(MediaType.APPLICATION_JSON_VALUE)
						.content("""
    					{
							"user": "someoneElse",
    					}
					"""))
				.andDo(print())
			.andExpect(status().isOk)

		assertThat(fakeApp.requestFrom(AssignTodoItem::class)).isEqualTo(
			AssignTodoItem.Request(
				TodoId.existing("abc-123"),
				UserId.existing("someoneElse"),
			)
		)
	}
}
