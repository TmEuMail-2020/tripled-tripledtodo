package io.tripled.todo.infra.rest

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import io.tripled.todo.TodoId
import io.tripled.todo.UserId
import io.tripled.todo.command.AssignTodoItem
import io.tripled.todo.command.CancelTodoItem
import io.tripled.todo.command.CreateTodoItem
import io.tripled.todo.command.FinishTodoItem
import io.tripled.todo.command.UpdateInformationInTodoItem
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.web.servlet.config.annotation.EnableWebMvc
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer


@EnableWebMvc
@Configuration
@Import(value = [RestExceptionHandler::class])
class RestConfiguration : WebMvcConfigurer {

    override fun configureMessageConverters(converters: MutableList<HttpMessageConverter<*>?>) {
        converters.add(jackson2HttpMessageConverter())
    }

    @Bean
    fun jackson2HttpMessageConverter(): MappingJackson2HttpMessageConverter? {
        val converter = MappingJackson2HttpMessageConverter()
        val builder = jacksonBuilder()
        builder.featuresToEnable(SerializationFeature.INDENT_OUTPUT);
        builder.modules(KotlinModule(), customSerialization())
        converter.objectMapper = builder.build()
        return converter
    }

    private fun customSerialization(): SimpleModule {
        val simpleModule = SimpleModule()
        simpleModule.addSerializer(TodoId::class.java, TodoIdSerializer())
        simpleModule.addDeserializer(UserId::class.java, UserIdDeserializer())
        return simpleModule
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
