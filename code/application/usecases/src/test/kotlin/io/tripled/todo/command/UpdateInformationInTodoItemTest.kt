package io.tripled.todo.command

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import io.tripled.todo.DomainException
import io.tripled.todo.TodoItemStatus
import io.tripled.todo.UserId
import io.tripled.todo.domain.TodoItem
import io.tripled.todo.mothers.Todos
import io.tripled.todo.testing.TodoItemTest


class UpdateInformationInTodoItemTest : TodoItemTest<UpdateInformationInTodoItem>(
        { todoItems, _ -> UpdateInformationInTodoItemCommand(todoItems) },
        { updateInformationInTodoItem, testTodoItems ->

    given("A todo item that's in progress") {
        val request = UpdateInformationInTodoItem.Request(Todos.paintingTheRoom.id,
                                                          "Paint living room",
                                                          "Paint living room pink")

        `when`("We update the information inside the todo item") {
            testTodoItems.assumeExisting = Todos.paintingTheRoom
            updateInformationInTodoItem.updateInformation(request)

            then("We verify that the todo item has been updated") {
                testTodoItems.lastSaved shouldBe Todos.paintingTheRoom.copy(
                    title = "Paint living room",
                    description = "Paint living room pink",
                )

                val dispatchedEvents = testTodoItems.dispatchedEvents
                dispatchedEvents shouldBe listOf(
                    TodoItem.TodoItemInformationUpdated(
                        Todos.paintingTheRoom.id,
                        "Paint living room",
                        "Paint living room pink",
                    )
                )
                val event = dispatchedEvents[0] as TodoItem.TodoItemInformationUpdated
                event.id shouldBe Todos.paintingTheRoom.id
                event.title shouldBe "Paint living room"
                event.description shouldBe "Paint living room pink"
            }
        }

        `when`("We attempt to update a finished todo item") {
            testTodoItems.assumeExisting = Todos.finishedPaintingTheRoom

            val exception = shouldThrow<DomainException> {
                updateInformationInTodoItem.updateInformation(request)
            }

            then("It should fail since we don't want them to change anymore") {
                exception shouldBe DomainException("Can't change todoItem '${Todos.paintingTheRoom.id.id}' it's 'FINISHED'")
            }
        }


        `when`("We attempt to update a cancelled todo item") {
            testTodoItems.assumeExisting = Todos.cancelledPaintingTheRoom

            val exception = shouldThrow<DomainException> {
                updateInformationInTodoItem.updateInformation(request)
            }

            then("It should fail since we don't want them to change anymore") {
                exception shouldBe DomainException("Can't change todoItem '${Todos.paintingTheRoom.id.id}' it's 'CANCELLED'")
            }
        }
    }
})
