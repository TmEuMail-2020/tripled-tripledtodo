package io.tripled.todo.command

import io.tripled.todo.TodoId

interface CreateTodoItem {

    fun create(request: Request): Response

    data class Request(val title: String, val description: String)
    data class Response(val id: TodoId)
}
