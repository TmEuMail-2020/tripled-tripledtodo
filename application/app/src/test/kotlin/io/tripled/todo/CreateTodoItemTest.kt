package io.tripled.todo

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.core.test.AssertionMode
import io.kotest.matchers.shouldBe
import io.tripled.todo.fakes.FakeTodoItemsRepository
import io.tripled.todo.fakes.TestTodoItems


class CreateTodoItemTest : BehaviorSpec() {
    init {
        assertions = AssertionMode.Error

        val fakeTodoItemRepository = FakeTodoItemsRepository()
        val testTodoItems: TestTodoItems = fakeTodoItemRepository
        val createTodoItem = CreateTodoItemCommand(fakeTodoItemRepository)

        given("A title and description of a todo item") {
            val title = "Painting"
            val description = "Paint living room white"
            val request = CreateTodoItem.Request(title, description)

            `when`("Creating the todo") {
                val response = createTodoItem.create(request)

                then("We verify that the todo item has been saved") {
                    testTodoItems.lastSaved shouldBe TodoItem.Snapshot(response.id, title, description)
                }
            }
        }
    }
}
