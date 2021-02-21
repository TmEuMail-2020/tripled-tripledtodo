package io.tripled.todo.infra.rest.testing

import io.tripled.todo.command.AssignTodoItem
import io.tripled.todo.command.CancelTodoItem
import io.tripled.todo.command.CreateTodoItem
import io.tripled.todo.command.FinishTodoItem
import io.tripled.todo.command.UpdateInformationInTodoItem
import io.tripled.todo.infra.rest.RestConfiguration
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import kotlin.reflect.KClass

@ExtendWith(SpringExtension::class)
@SpringBootTest(classes =
                [RestConfiguration::class,
                RestTest.FakeApplication::class])
@AutoConfigureMockMvc
class RestTest {

    @Autowired lateinit var mockMvc:MockMvc
    @Autowired lateinit var fakeApp: FakeApplication

    @Configuration
    class FakeApplication {
        private val requests = HashMap<KClass<*>, Any>()
        private val responses = HashMap<KClass<*>, Any>()

        fun responseFor(clazz: KClass<*>, request: Any) = responses.put(clazz, request)

        fun requestFrom(clazz: KClass<*>) = requests[clazz]

        @Bean
        fun createTodoItem() = object : CreateTodoItem {
            override fun create(request: CreateTodoItem.Request)
                    = reqRes(request, CreateTodoItem::class) as CreateTodoItem.Response
        }

        @Bean
        fun cancelTodoItem() = object : CancelTodoItem {
            override fun cancel(request: CancelTodoItem.Request) {
                reqRes(request, CancelTodoItem::class)
            }
        }

        @Bean
        fun finishTodoItem() = object : FinishTodoItem {
            override fun finish(request: FinishTodoItem.Request) {
                reqRes(request, FinishTodoItem::class)
            }
        }

        @Bean
        fun assignTodoItem() = object : AssignTodoItem {
            override fun assign(request: AssignTodoItem.Request) {
                reqRes(request, AssignTodoItem::class)
            }
        }

        @Bean
        fun updateInformationInTodoItem() = object : UpdateInformationInTodoItem {
            override fun updateInformation(request: UpdateInformationInTodoItem.Request) {
                reqRes(request, UpdateInformationInTodoItem::class)
            }
        }

        private fun reqRes(request: Any, klass: KClass<*>): Any? {
            requests[klass] = request
            return responses[klass]
        }
    }
}