package io.tripled.todo.infra.rest

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import io.tripled.todo.TodoId
import io.tripled.todo.command.CreateTodoItem
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.web.servlet.config.annotation.EnableWebMvc
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer


@EnableWebMvc
@Configuration
@Import(value = [InfraRestConfig.JacksonWebConfiguration::class])
class InfraRestConfig : WebMvcConfigurer {
    @Bean
    fun todoItemRestController(createTodoItem: CreateTodoItem) = TodoItemRestController(createTodoItem)

    @Bean
    fun objectMapper() = ObjectMapper()

    @Configuration
    class JacksonWebConfiguration constructor(objectMapper: ObjectMapper) {
        init {
            objectMapper.registerModule(KotlinModule())
            val module = SimpleModule()
            module.addSerializer(TodoId::class.java, TodoIdSerializer())
            objectMapper.registerModule(module)
        }
    }

    class TodoIdSerializer : JsonSerializer<TodoId>() {
        override fun serialize(
            value: TodoId,
            jgen: JsonGenerator,
            provider: SerializerProvider
        ) {
            jgen.writeString(value.id)
        }
    }
}