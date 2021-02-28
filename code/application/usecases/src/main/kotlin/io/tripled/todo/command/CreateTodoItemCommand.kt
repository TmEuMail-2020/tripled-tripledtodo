package io.tripled.todo.command

import io.tripled.todo.domain.TodoItem
import io.tripled.todo.domain.TodoItems

class CreateTodoItemCommand(private val todoItems: TodoItems,
                            private val dispatch: (Any) -> Unit) : CreateTodoItem {
    override fun create(request: CreateTodoItem.Request): CreateTodoItem.Response {
        val createdTodoItem = TodoItem.createNew(request.title, request.description, dispatch)

        todoItems.save(createdTodoItem)

        return CreateTodoItem.Response(createdTodoItem.id)
    }

}
