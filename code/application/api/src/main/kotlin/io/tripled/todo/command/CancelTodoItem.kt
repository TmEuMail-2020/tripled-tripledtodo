package io.tripled.todo.command

import io.tripled.todo.TodoId

interface CancelTodoItem {
    fun cancel(request: Request)

    data class Request(val todoId: TodoId)

}
