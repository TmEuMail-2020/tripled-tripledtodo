package io.tripled.todo

import io.kotest.matchers.shouldBe
import io.tripled.todo.mothers.Todos
import io.tripled.todo.testing.TodoItemTest


class AssignTodoItemTest : TodoItemTest({ fakeTodoItems, testTodoItems ->
    var userExists = true
    val assignTodoItem: AssignTodoItem = AssignTodoItemCommand(fakeTodoItems) {
        (_) -> userExists
    }

    given("A todo item that's in progress") {
        testTodoItems.assumeExisting = Todos.paintingTheRoom
        val request = AssignTodoItem.Request(Todos.paintingTheRoom.id, UserId("someoneElse"))

        `when`("Assigning the todo to an existing user") {
            userExists = true
            assignTodoItem.assign(request)

            then("We verify that the todo item has been assigned") {
                testTodoItems.lastSaved shouldBe Todos.paintingTheRoom.copy(
                    assignee = UserId("someoneElse")
                )
            }
        }

        `when`("Assigning the todo to a non-existing user") {
            userExists = false
            assignTodoItem.assign(request)

            then("We verify that the todo item has been assigned") {
                testTodoItems.lastSaved shouldBe Todos.paintingTheRoom.copy(
                    assignee = null
                )
            }
        }
    }
})