package io.tripled.todo.query

import io.tripled.todo.TodoId
import io.tripled.todo.TodoItemStatus
import io.tripled.todo.UserId

interface GetTodoItems {

    fun getAll(): Response

    data class Response(val todoItems: List<TodoItem>){
        data class TodoItem(
            val id: TodoId,
            val title: String,
            val description: String,
            val assignee: UserId?,
            val status: TodoItemStatus,
        )
    }
}
