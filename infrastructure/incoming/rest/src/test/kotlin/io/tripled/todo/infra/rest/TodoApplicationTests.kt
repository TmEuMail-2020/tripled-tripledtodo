package io.tripled.todo.infra.rest

import io.tripled.todo.TodoId
import io.tripled.todo.command.CreateTodoItem
import org.assertj.core.api.Assertions.assertThat
import org.hamcrest.CoreMatchers.containsString
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@ExtendWith(SpringExtension::class)
@SpringBootTest(classes = [InfraRestConfig::class])
@AutoConfigureMockMvc
class TodoApplicationTests (
		@Autowired private val mockMvc: MockMvc
	) {

	val application = FakeApplication()

	class FakeApplication {
		lateinit var createRequest: CreateTodoItem.Request

		fun createTodoItem() = object : CreateTodoItem {
			override fun create(request: CreateTodoItem.Request): CreateTodoItem.Response {
				createRequest = request
				return CreateTodoItem.Response(TodoId.existing("generated-todo-id"))
			}
		}
	}

	@Test
	fun `posting a new Todo item tells us it's created`() {
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
			.andExpect(content().string(containsString("generated-todo-id")))
			.andExpect(status().isCreated)

		assertThat(application.createRequest).isEqualTo(
			CreateTodoItem.Request(
				"prep talk",
				"prepare a kotlin talk"
			)
		)
	}

}
