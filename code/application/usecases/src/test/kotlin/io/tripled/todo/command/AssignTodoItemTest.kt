package io.tripled.todo.command

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import io.tripled.todo.DomainException
import io.tripled.todo.TodoItemStatus
import io.tripled.todo.UserId
import io.tripled.todo.domain.TodoItem
import io.tripled.todo.mothers.Todos
import io.tripled.todo.testing.TodoItemTest


class AssignTodoItemTest : TodoItemTest<AssignTodoItem>(
        { testTodoItems, _ -> AssignTodoItemCommand(testTodoItems){
        (userId) -> when(userId){
            "someoneElse" -> true
            else -> false
        }
    }},
        { assignTodoItem, testTodoItems ->


    given("A todo item that's in progress") {
        testTodoItems.assumeExisting = Todos.paintingTheRoom

        `when`("Assigning the todo to an existing user") {
            val request = AssignTodoItem.Request(Todos.paintingTheRoom.id, UserId("someoneElse"))
            assignTodoItem.assign(request)

            then("We verify that the todo item has been assigned") {
                testTodoItems.lastSaved shouldBe Todos.paintingTheRoom.copy(
                    assignee = UserId("someoneElse"),
                    status = TodoItemStatus.ASSIGNED,
                )

                val dispatchedEvents = testTodoItems.dispatchedEvents
                dispatchedEvents shouldBe listOf(
                    TodoItem.TodoItemAssigned(
                        Todos.paintingTheRoom.id,
                        TodoItemStatus.ASSIGNED,
                        UserId("someoneElse"),
                    )
                )
                val event = dispatchedEvents[0] as TodoItem.TodoItemAssigned
                event.id shouldBe Todos.paintingTheRoom.id
                event.status shouldBe TodoItemStatus.ASSIGNED
                event.assignee shouldBe UserId("someoneElse")
            }
        }

        `when`("Assigning the todo to a non-existing user") {
            val request = AssignTodoItem.Request(Todos.paintingTheRoom.id, UserId("someoneWhoDoesntExist"))

            val exception = shouldThrow<DomainException> {
                assignTodoItem.assign(request)
            }

            then("We verify that the todo item could not be assigned to a user that doesn't exist in our system") {
                exception shouldBe DomainException("Can't assign todoItem '${Todos.paintingTheRoom.id.id}' to a non-existing user 'someoneWhoDoesntExist'")
            }
        }
    }
})
