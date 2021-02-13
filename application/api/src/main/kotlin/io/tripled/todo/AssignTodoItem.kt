package io.tripled.todo

interface AssignTodoItem {
    fun assign(request: Request)

    class Request(val todoId: TodoId, val userId: UserId)

}
