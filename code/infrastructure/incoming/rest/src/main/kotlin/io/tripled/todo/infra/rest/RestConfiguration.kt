package io.tripled.todo.infra.rest

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import io.tripled.todo.TodoId
import io.tripled.todo.command.CancelTodoItem
import io.tripled.todo.command.CreateTodoItem
import io.tripled.todo.command.FinishTodoItem
import io.tripled.todo.command.UpdateInformationInTodoItem
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.web.servlet.config.annotation.EnableWebMvc
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer


@EnableWebMvc
@Configuration
class InfraRestConfig : WebMvcConfigurer {
    @Bean
    fun todoItemRestController(
                                createTodoItem: CreateTodoItem,
                                cancelTodoItem: CancelTodoItem,
                                finishTodoItem: FinishTodoItem,
                                updateInformationInTodoItem: UpdateInformationInTodoItem,
                            )
                    = TodoItemRestController(createTodoItem,
                                             cancelTodoItem,
                                             finishTodoItem,
                                             updateInformationInTodoItem)

    override fun configureMessageConverters(converters: MutableList<HttpMessageConverter<*>?>) {
        converters.add(jackson2HttpMessageConverter())
    }

    @Bean
    fun jackson2HttpMessageConverter(): MappingJackson2HttpMessageConverter? {
        val converter = MappingJackson2HttpMessageConverter()
        val builder = jacksonBuilder()
        val simpleModule = SimpleModule()
        simpleModule.addSerializer(TodoId::class.java, TodoIdSerializer())
        builder.modules(KotlinModule(), simpleModule)
        converter.objectMapper = builder.build()
        return converter
    }

    fun jacksonBuilder(): Jackson2ObjectMapperBuilder {
        val builder = Jackson2ObjectMapperBuilder()
        builder.propertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE)
        return builder
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