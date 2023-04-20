package io.tripled.todo.command

import io.kotest.matchers.shouldBe
import io.tripled.todo.TodoItemStatus
import io.tripled.todo.domain.TodoItem
import io.tripled.todo.mothers.Todos
import io.tripled.todo.testing.TodoItemTest


class CreateTodoItemTest : TodoItemTest<CreateTodoItem>(
        { todoItems, dispatchEvent -> CreateTodoItemCommand(todoItems, dispatchEvent) },
        { createTodoItem, testTodoItems ->


    given("A title and description of a todo item") {
        val title = "Painting"
        val description = "Paint living room white"
        val request = CreateTodoItem.Request(title, description)

        `when`("Creating the todo") {
            val response = createTodoItem.create(request)

            then("We verify that the todo item has been saved correctly") {
                testTodoItems.lastSaved shouldBe Todos.paintingTheRoom.copy(id = response.id)
                val dispatchedEvents = testTodoItems.dispatchedEvents
                dispatchedEvents shouldBe listOf(
                    TodoItem.TodoItemCreated(
                        response.id,
                        "Painting",
                        "Paint living room white",
                        TodoItemStatus.CREATED,
                    )
                )

                // technical noise
                val event = dispatchedEvents[0] as TodoItem.TodoItemCreated
                event.id shouldBe response.id
                event.status shouldBe TodoItemStatus.CREATED
                event.title shouldBe "Painting"
                event.description shouldBe "Paint living room white"
            }
        }
    }
})