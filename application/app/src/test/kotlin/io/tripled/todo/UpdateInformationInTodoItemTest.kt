package io.tripled.todo

import io.kotest.matchers.shouldBe
import io.tripled.todo.mothers.Todos
import io.tripled.todo.testing.TodoItemTest


class UpdateInformationInTodoItemTest : TodoItemTest({ fakeTodoItems, testTodoItems ->
    val updateInformationInTodoItem: UpdateInformationInTodoItem = UpdateInformationInTodoItemCommand(fakeTodoItems)

    given("A todo item that's in progress") {
        fakeTodoItems.assumeExisting = Todos.paintingTheRoom
        val request = UpdateInformationInTodoItem.Request(Todos.paintingTheRoom.id,
                                                          "Paint living room",
                                                          "Paint living room pink")

        `when`("We update the information inside the todo item") {
            updateInformationInTodoItem.updateInformation(request)

            then("We verify that the todo item has been updated") {
                testTodoItems.lastSaved shouldBe Todos.paintingTheRoom.copy(
                    title = "Paint living room",
                    description = "Paint living room pink",
                )
            }
        }
    }
})
