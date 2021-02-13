package io.tripled.todo

interface FinishTodoItem {
    fun finish(request: Request)

    class Request(val todoId: TodoId)

}
