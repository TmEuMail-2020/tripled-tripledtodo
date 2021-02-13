package io.tripled.todo

import io.kotest.matchers.shouldBe
import io.tripled.todo.mothers.Todos
import io.tripled.todo.testing.TodoItemTest


class AssignTodoItemTest : TodoItemTest({ fakeTodoItems, testTodoItems ->
    val assignTodoItem: AssignTodoItem = AssignTodoItemCommand(fakeTodoItems)

    given("A todo item that's in progress") {
        testTodoItems.assumeExisting = Todos.paintingTheRoom
        val request = AssignTodoItem.Request(Todos.paintingTheRoom.id, UserId("someoneElse"))

        `when`("Assigning the todo") {
            assignTodoItem.assign(request)

            then("We verify that the todo item has been assigned") {
                testTodoItems.lastSaved shouldBe Todos.paintingTheRoom.copy(
                    assignee = UserId("someoneElse")
                )
            }
        }
    }
})