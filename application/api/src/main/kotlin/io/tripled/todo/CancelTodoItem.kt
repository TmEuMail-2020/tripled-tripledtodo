package io.tripled.todo

interface CancelTodoItem {
    fun cancel(request: Request)

    class Request(val todoId: TodoId)

}
