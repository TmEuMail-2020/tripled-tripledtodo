package io.tripled.todo.infra.rest

import io.tripled.todo.TodoId
import io.tripled.todo.command.CreateTodoItem
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import kotlin.reflect.KClass

@ExtendWith(SpringExtension::class)
@SpringBootTest(classes = [InfraRestConfig::class, TodoApplicationTests.FakeApplication::class])
@AutoConfigureMockMvc
class TodoApplicationTests (
		@Autowired private val mockMvc: MockMvc,
		@Autowired private val fakeApp: FakeApplication
	) {

	@Configuration
	class FakeApplication {
		private val requests = HashMap<KClass<*>, Any>()
		private val responses = HashMap<KClass<*>, Any>()

		fun responseFor(clazz: KClass<*>, request: Any) = responses.put(clazz, request)

		fun requestFrom(clazz: KClass<*>) = requests[clazz]

		@Bean
		fun createTodoItem() = object : CreateTodoItem {
			override fun create(request: CreateTodoItem.Request): CreateTodoItem.Response {
				requests[CreateTodoItem::class] = request
				return responses[CreateTodoItem::class] as CreateTodoItem.Response
			}
		}
	}

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

}
