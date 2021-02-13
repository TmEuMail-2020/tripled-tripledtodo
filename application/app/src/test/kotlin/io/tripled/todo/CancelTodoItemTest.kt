package io.tripled.todo

import io.kotest.matchers.shouldBe
import io.tripled.todo.domain.TodoItem
import io.tripled.todo.testing.TodoItemTest


class CancelTodoItemTest : TodoItemTest({fakeTodoItems, testTodoItems ->
    val cancelTodoItem: CancelTodoItem = CancelTodoItemCommand(fakeTodoItems)

    given("A todo item that's in progress") {
        val todoId = TodoId.create("abc-123")
        val existingTodoItem = TodoItem.Snapshot(todoId, "title", "description", TodoItem.Status.CREATED)
        testTodoItems.assumeExisting = existingTodoItem
        val request = CancelTodoItem.Request(todoId)

        `when`("Cancelling the todo") {
            cancelTodoItem.cancel(request)

            then("We verify that the todo item has been cancelled") {
                testTodoItems.lastSaved shouldBe TodoItem.Snapshot(todoId,
                    "title", "description",
                    TodoItem.Status.CANCELLED)
            }
        }
    }
})