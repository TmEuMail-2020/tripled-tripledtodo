package io.tripled.todo.query

import io.tripled.todo.TodoId
import io.tripled.todo.TodoItemStatus
import io.tripled.todo.UserId

interface GetTodoItem {

    fun get(request: Request): Response

    data class Request(val todoId: TodoId)

    data class Response(val todoItem: TodoItem){
        data class TodoItem(
            val id: TodoId,
            val title: String,
            val description: String,
            val assignee: UserId?,
            val created: TodoItemStatus,
        )
    }
}
