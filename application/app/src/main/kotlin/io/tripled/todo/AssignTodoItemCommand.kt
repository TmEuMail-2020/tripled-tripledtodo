package io.tripled.todo

import io.tripled.todo.domain.TodoItems

class AssignTodoItemCommand(private val todoItems: TodoItems) : AssignTodoItem {
    override fun assign(request: AssignTodoItem.Request) {
        val todoItem = todoItems.find(request.todoId)!!
        todoItem.assign(request.userId)
        todoItems.save(todoItem)
    }

}
