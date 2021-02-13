package io.tripled.todo

import io.kotest.matchers.shouldBe
import io.tripled.todo.domain.TodoItem
import io.tripled.todo.testing.TodoItemTest


class FinishTodoItemTest : TodoItemTest({ fakeTodoItems, testTodoItems ->
    val finishTodoItem: FinishTodoItem = FinishTodoItemCommand(fakeTodoItems)

    given("A todo item that's in progress") {
        val todoId = TodoId.create("abc-123")
        val existingTodoItem = TodoItem.Snapshot(todoId, "title", "description", TodoItem.Status.CREATED)
        testTodoItems.assumeExisting = existingTodoItem
        val request = FinishTodoItem.Request(todoId)

        `when`("Finishing the todo") {
            finishTodoItem.finish(request)

            then("We verify that the todo item has been finished") {
                testTodoItems.lastSaved shouldBe TodoItem.Snapshot(todoId,
                                                                    "title", "description",
                                                                    TodoItem.Status.FINISHED)
            }
        }
    }
})
