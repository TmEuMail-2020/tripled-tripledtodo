package io.tripled.todo.command

import io.tripled.todo.UserId
import io.tripled.todo.domain.TodoItems

class AssignTodoItemCommand(
    private val todoItems: TodoItems,
    dispatchEvent: (Any) -> Unit,
    private val userExists: (UserId) -> Boolean
) : AssignTodoItem {
    override fun assign(request: AssignTodoItem.Request) {
        val todoItem = todoItems.find(request.todoId)!!
        todoItem.assign(request.userId, userExists)
        todoItems.save(todoItem)
    }

}
