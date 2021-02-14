package io.tripled.todo

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import io.tripled.todo.mothers.Todos
import io.tripled.todo.testing.TodoItemTest


class UpdateInformationInTodoItemTest : TodoItemTest({ fakeTodoItems, testTodoItems ->
    val updateInformationInTodoItem: UpdateInformationInTodoItem = UpdateInformationInTodoItemCommand(fakeTodoItems)

    given("A todo item that's in progress") {
        val request = UpdateInformationInTodoItem.Request(Todos.paintingTheRoom.id,
                                                          "Paint living room",
                                                          "Paint living room pink")

        `when`("We update the information inside the todo item") {
            fakeTodoItems.assumeExisting = Todos.paintingTheRoom
            updateInformationInTodoItem.updateInformation(request)

            then("We verify that the todo item has been updated") {
                testTodoItems.lastSaved shouldBe Todos.paintingTheRoom.copy(
                    title = "Paint living room",
                    description = "Paint living room pink",
                )
            }
        }

        `when`("We attempt to update a finished todo item") {
            fakeTodoItems.assumeExisting = Todos.finishedPaintingTheRoom

            val exception = shouldThrow<DomainException> {
                updateInformationInTodoItem.updateInformation(request)
            }

            then("It should fail since we don't want them to change anymore") {
                exception shouldBe DomainException("Can't change todoItem '${Todos.paintingTheRoom.id.id}' it's 'FINISHED'")
            }
        }
    }
})
