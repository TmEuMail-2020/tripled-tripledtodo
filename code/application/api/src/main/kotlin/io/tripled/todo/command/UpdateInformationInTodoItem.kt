package io.tripled.todo.command

import io.tripled.todo.TodoId

interface UpdateInformationInTodoItem {
    fun updateInformation(request: Request)

    data class Request(val todoId: TodoId,
                  val title: String,
                  val description: String)
}
