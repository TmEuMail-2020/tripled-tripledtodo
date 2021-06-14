package io.tripled.todo.command

import io.kotest.matchers.shouldBe
import io.tripled.todo.TodoItemStatus
import io.tripled.todo.domain.TodoItem
import io.tripled.todo.mothers.Todos
import io.tripled.todo.testing.TodoItemTest


class FinishTodoItemTest : TodoItemTest<FinishTodoItem>(
        { todoItems, _ ->  FinishTodoItemCommand(todoItems)},
        { finishTodoItem, testTodoItems ->

    given("A todo item that's in progress") {
        testTodoItems.assumeExisting = Todos.paintingTheRoom
        val request = FinishTodoItem.Request(Todos.paintingTheRoom.id)

        `when`("Finishing the todo") {
            finishTodoItem.finish(request)

            then("We verify that the todo item has been finished") {
                testTodoItems.lastSaved shouldBe Todos.finishedPaintingTheRoom

                val dispatchedEvents = testTodoItems.dispatchedEvents
                dispatchedEvents shouldBe listOf(
                    TodoItem.TodoItemFinished(
                        Todos.paintingTheRoom.id,
                        TodoItemStatus.FINISHED,
                    )
                )
                val event = dispatchedEvents[0] as TodoItem.TodoItemFinished
                event.id shouldBe Todos.paintingTheRoom.id
                event.status shouldBe TodoItemStatus.FINISHED
            }
        }
    }
})
