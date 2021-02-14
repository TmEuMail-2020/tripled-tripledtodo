package io.tripled.todo.query

import io.kotest.matchers.shouldBe
import io.tripled.todo.TodoId
import io.tripled.todo.mothers.Todos
import io.tripled.todo.testing.TodoItemTest


class GetTodoItemsTest : TodoItemTest({ fakeTodoItems, testTodoItems ->
    val getTodoItems: GetTodoItems = GetTodoItemsQuery(fakeTodoItems)

    given("Some todo items") {
        fakeTodoItems.assumeMultipleExisting = listOf(Todos.paintingTheRoom,
            Todos.cleaningTheGutter)

        `when`("Getting them") {
            val response = getTodoItems.getAll()

            then("We should have gotten them all") {
                response.todoItems shouldBe listOf(
                    GetTodoItems.Response.TodoItem(
                        TodoId.existing("abc-123"),
                        "Painting",
                        "Paint living room white",
                        null,
                        //TodoItem.Status.CREATED,
                    ),
                    GetTodoItems.Response.TodoItem(
                        TodoId.existing("xyz-987"),
                        "Cleaning gutter",
                        "Gutters need to be cleaned on the front and back side of the house",
                        null,
                        //TodoItem.Status.CREATED,
                    ),
                )
            }
        }
    }
})
