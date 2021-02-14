package io.tripled.todo.command

import io.tripled.todo.domain.TodoItems

class UpdateInformationInTodoItemCommand(private val todoItems: TodoItems) : UpdateInformationInTodoItem {
    override fun updateInformation(request: UpdateInformationInTodoItem.Request) {
        val todoItem = todoItems.find(request.todoId)!!
        todoItem.updateInformation(request.title, request.description)
        todoItems.save(todoItem)
    }

}
