package io.tripled.todo.command

import io.tripled.todo.domain.TodoItems

class FinishTodoItemCommand(private val todoItems: TodoItems) : FinishTodoItem {
    override fun finish(request: FinishTodoItem.Request) {
        val todoItem = todoItems.find(request.todoId)!!
        todoItem.finish()
        todoItems.save(todoItem)
    }

}
