package io.tripled.todo.query

import io.kotest.matchers.shouldBe
import io.tripled.todo.TodoId
import io.tripled.todo.TodoItemStatus
import io.tripled.todo.UserId
import io.tripled.todo.mothers.Todos
import io.tripled.todo.testing.TodoItemTest


class GetTodoItemTest : TodoItemTest<GetTodoItem>(
        { todoItems, _ -> GetTodoItemQuery(todoItems) },
        { getTodoItem, testTodoItems ->

    given("Some todo items") {
        testTodoItems.assumeExisting = Todos.paintingTheRoom.copy(assignee = UserId.existing("someUser"))
        val request = GetTodoItem.Request(TodoId.existing("abc-123"))

        `when`("Getting them") {
            val response = getTodoItem.get(request)

            then("We should have gotten them all") {
                val expectedTodoItem = GetTodoItem.Response.TodoItem(
                    TodoId.existing("abc-123"),
                    "Painting",
                    "Paint living room white",
                    UserId.existing("someUser"),
                    TodoItemStatus.CREATED,
                )
                response shouldBe GetTodoItem.Response(expectedTodoItem)
                response.todoItem.id shouldBe expectedTodoItem.id
                response.todoItem.status shouldBe expectedTodoItem.status
                response.todoItem.title shouldBe expectedTodoItem.title
                response.todoItem.description shouldBe expectedTodoItem.description
                response.todoItem.assignee shouldBe expectedTodoItem.assignee
            }
        }
    }
})
