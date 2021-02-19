package io.tripled.todo.command

import io.tripled.todo.TodoId

interface FinishTodoItem {
    fun finish(request: Request)

    data class Request(val todoId: TodoId)
}
