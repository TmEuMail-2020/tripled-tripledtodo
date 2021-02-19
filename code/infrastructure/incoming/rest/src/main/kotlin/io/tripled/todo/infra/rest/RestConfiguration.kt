package io.tripled.todo.infra.rest

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.*
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import io.tripled.todo.TodoId
import io.tripled.todo.UserId
import io.tripled.todo.command.*
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
                                assignTodoItem: AssignTodoItem,
                            )
                    = TodoItemRestController(createTodoItem,
                                             cancelTodoItem,
                                             finishTodoItem,
                                             updateInformationInTodoItem,
                                             assignTodoItem)

    override fun configureMessageConverters(converters: MutableList<HttpMessageConverter<*>?>) {
        converters.add(jackson2HttpMessageConverter())
    }

    @Bean
    fun jackson2HttpMessageConverter(): MappingJackson2HttpMessageConverter? {
        val converter = MappingJackson2HttpMessageConverter()
        val builder = jacksonBuilder()
        val simpleModule = SimpleModule()
        simpleModule.addSerializer(TodoId::class.java, TodoIdSerializer())
        simpleModule.addDeserializer(UserId::class.java, UserIdDeserializer())
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

    class UserIdDeserializer : JsonDeserializer<UserId>() {
        override fun deserialize(p: JsonParser, ctxt: DeserializationContext): UserId {
            val node: JsonNode = p.codec
                .readTree(p)
            val id = node.asText()
            return UserId.existing(id)
        }
    }
}