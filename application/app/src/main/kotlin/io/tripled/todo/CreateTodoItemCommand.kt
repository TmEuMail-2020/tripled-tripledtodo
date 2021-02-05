package io.tripled.todo

import io.tripled.todo.domain.TodoItem
import io.tripled.todo.domain.TodoItemsRepository

class CreateTodoItemCommand(private val todoItemRepository: TodoItemsRepository) : CreateTodoItem {
    override fun create(request: CreateTodoItem.Request): CreateTodoItem.Response {
        val createdTodoItem = TodoItem(request.title, request.description)

        todoItemRepository.save(createdTodoItem)

        return CreateTodoItem.Response(createdTodoItem.id)
    }

}
