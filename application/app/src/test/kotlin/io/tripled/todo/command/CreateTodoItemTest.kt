package io.tripled.todo.command

import io.kotest.matchers.shouldBe
import io.tripled.todo.command.CreateTodoItem
import io.tripled.todo.command.CreateTodoItemCommand
import io.tripled.todo.mothers.Todos
import io.tripled.todo.testing.TodoItemTest


class CreateTodoItemTest : TodoItemTest({ fakeTodoItems, testTodoItems ->
    val createTodoItem: CreateTodoItem = CreateTodoItemCommand(fakeTodoItems)

    given("A title and description of a todo item") {
        val title = "Painting"
        val description = "Paint living room white"
        val request = CreateTodoItem.Request(title, description)

        `when`("Creating the todo") {
            val response = createTodoItem.create(request)

            then("We verify that the todo item has been saved correctly") {
                testTodoItems.lastSaved shouldBe Todos.paintingTheRoom.copy(id = response.id)
            }
        }
    }
})