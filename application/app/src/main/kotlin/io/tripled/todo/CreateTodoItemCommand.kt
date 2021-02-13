package io.tripled.todo

import io.tripled.todo.domain.TodoItem
import io.tripled.todo.domain.TodoItems

class CreateTodoItemCommand(private val todoItems: TodoItems) : CreateTodoItem {
    override fun create(request: CreateTodoItem.Request): CreateTodoItem.Response {
        val createdTodoItem = TodoItem.createNew(request.title, request.description)

        todoItems.save(createdTodoItem)

        return CreateTodoItem.Response(createdTodoItem.id)
    }

}
