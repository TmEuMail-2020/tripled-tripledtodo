package io.tripled.todo

import io.kotest.matchers.shouldBe
import io.tripled.todo.mothers.Todos
import io.tripled.todo.testing.TodoItemTest


class CancelTodoItemTest : TodoItemTest({fakeTodoItems, testTodoItems ->
    val cancelTodoItem: CancelTodoItem = CancelTodoItemCommand(fakeTodoItems)

    given("A todo item that's in progress") {
        testTodoItems.assumeExisting = Todos.paintingTheRoom
        val request = CancelTodoItem.Request(Todos.paintingTheRoom.id)

        `when`("Cancelling the todo") {
            cancelTodoItem.cancel(request)

            then("We verify that the todo item has been cancelled") {
                testTodoItems.lastSaved shouldBe Todos.cancelledPaintingTheRoom
            }
        }
    }
})