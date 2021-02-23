package io.tripled.todo.command

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import io.tripled.todo.DomainException
import io.tripled.todo.mothers.Todos
import io.tripled.todo.testing.TodoItemTest


class CancelTodoItemTest : TodoItemTest<CancelTodoItem>(
        { todoItems -> CancelTodoItemCommand(todoItems) },
        { cancelTodoItem, testTodoItems ->

    given("A todo item that's in progress") {
        testTodoItems.assumeExisting = Todos.paintingTheRoom
        val request = CancelTodoItem.Request(Todos.paintingTheRoom.id)

        `when`("Cancelling the todo") {
            cancelTodoItem.cancel(request)

            then("We verify that the todo item has been cancelled") {
                testTodoItems.lastSaved shouldBe Todos.cancelledPaintingTheRoom
            }
        }


        `when`("Cancelling an already finished todo") {
            testTodoItems.assumeExisting = Todos.finishedPaintingTheRoom

            val exception = shouldThrow<DomainException> {
                cancelTodoItem.cancel(request)
            }

            then("It should have failed, no reason to cancel already finished todos") {
                exception shouldBe DomainException("Can't cancel finished todoItem '${Todos.paintingTheRoom.id.id}'")
            }
        }
    }
})