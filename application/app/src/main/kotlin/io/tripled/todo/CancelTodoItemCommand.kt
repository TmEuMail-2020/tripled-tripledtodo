package io.tripled.todo

import io.tripled.todo.domain.TodoItems

class CancelTodoItemCommand(private val todoItems: TodoItems) : CancelTodoItem {
    override fun cancel(request: CancelTodoItem.Request) {
        val todoItem = todoItems.find(request.todoId)!!
        todoItem.cancel()
        todoItems.save(todoItem)
    }

}
