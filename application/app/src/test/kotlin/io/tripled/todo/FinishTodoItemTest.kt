package io.tripled.todo

import io.kotest.matchers.shouldBe
import io.tripled.todo.mothers.Todos
import io.tripled.todo.testing.TodoItemTest


class FinishTodoItemTest : TodoItemTest({ fakeTodoItems, testTodoItems ->
    val finishTodoItem: FinishTodoItem = FinishTodoItemCommand(fakeTodoItems)

    given("A todo item that's in progress") {
        fakeTodoItems.assumeExisting = Todos.paintingTheRoom
        val request = FinishTodoItem.Request(Todos.paintingTheRoom.id)

        `when`("Finishing the todo") {
            finishTodoItem.finish(request)

            then("We verify that the todo item has been finished") {
                testTodoItems.lastSaved shouldBe Todos.finishedPaintingTheRoom
            }
        }
    }
})
