package io.tripled.todo.query

import io.kotest.matchers.shouldBe
import io.tripled.todo.TodoId
import io.tripled.todo.TodoItemStatus
import io.tripled.todo.UserId
import io.tripled.todo.mothers.Todos
import io.tripled.todo.testing.TodoItemTest


class GetTodoItemsTest : TodoItemTest<GetTodoItems>(
        { todoItems, _ -> GetTodoItemsQuery(todoItems) },
        { getTodoItems, testTodoItems ->

    given("Some todo items") {
        testTodoItems.assumeMultipleExisting = listOf(
            Todos.paintingTheRoom.copy(assignee = UserId.existing("someUser")),
            Todos.cleaningTheGutter
        )

        `when`("Getting them") {
            val response = getTodoItems.getAll()

            then("We should have gotten them all") {
                val expectedTodoItem = GetTodoItems.Response.TodoItem(
                    TodoId.existing("abc-123"),
                    "Painting",
                    "Paint living room white",
                    UserId.existing("someUser"),
                    TodoItemStatus.CREATED,
                )
                val returnedFirstTodoItem = response.todoItems[0]
                returnedFirstTodoItem.id shouldBe expectedTodoItem.id
                returnedFirstTodoItem.status shouldBe expectedTodoItem.status
                returnedFirstTodoItem.title shouldBe expectedTodoItem.title
                returnedFirstTodoItem.description shouldBe expectedTodoItem.description
                returnedFirstTodoItem.assignee shouldBe expectedTodoItem.assignee

                response.todoItems shouldBe listOf(
                    expectedTodoItem,
                    GetTodoItems.Response.TodoItem(
                        TodoId.existing("xyz-987"),
                        "Cleaning gutter",
                        "Gutters need to be cleaned on the front and back side of the house",
                        null,
                        TodoItemStatus.CREATED,
                    ),
                )
            }
        }
    }
})
