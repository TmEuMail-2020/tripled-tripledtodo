package io.tripled.todo.query

import io.kotest.matchers.shouldBe
import io.tripled.todo.TodoId
import io.tripled.todo.mothers.Todos
import io.tripled.todo.testing.TodoItemTest


class GetTodoItemTest : TodoItemTest({ fakeTodoItems, testTodoItems ->
    val getTodoItems: GetTodoItem = GetTodoItemQuery(fakeTodoItems)

    given("Some todo items") {
        fakeTodoItems.assumeMultipleExisting = listOf(Todos.paintingTheRoom,
            Todos.cleaningTheGutter)
        val request = GetTodoItem.Request(TodoId.existing("abc-123"))

        `when`("Getting them") {
            val response = getTodoItem.get(request)

            then("We should have gotten them all") {
                response shouldBe GetTodoItem.Response.TodoItem(
                        TodoId.existing("abc-123"),
                        "Painting",
                        "Paint living room white",
                        null,
                        //TodoItem.Status.CREATED,
                    )
            }
        }
    }
})
