package io.tripled.todo

interface FinishTodoItem {
    fun create(request: Request)

    class Request(private val todoId: TodoId)

}
