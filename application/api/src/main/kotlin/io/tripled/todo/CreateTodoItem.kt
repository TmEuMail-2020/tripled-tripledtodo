package io.tripled.todo

interface CreateTodoItem {

    fun create(request: Request): Response

    data class Request(val title: String, val description: String)
    data class Response(val id: TodoId)
}
