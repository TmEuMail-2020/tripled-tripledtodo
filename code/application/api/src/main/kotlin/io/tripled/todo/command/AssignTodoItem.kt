package io.tripled.todo.command

import io.tripled.todo.TodoId
import io.tripled.todo.UserId

interface AssignTodoItem {
    fun assign(request: Request)

    data class Request(val todoId: TodoId, val userId: UserId)

}
