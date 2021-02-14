package io.tripled.todo

interface UpdateInformationInTodoItem {
    fun updateInformation(request: Request)

    class Request(val todoId: TodoId,
                  val title: String,
                  val description: String)

}
