package io.tripled.todo

interface FinishTodoItem {
    fun create(request: Request)

    class Request(val todoId: TodoId)

}
