package io.tripled.todo

import io.tripled.todo.domain.TodoItemsRepository

class CreateTodoItemCommand(todoItemRepository: TodoItemsRepository) : CreateTodoItem {
    override fun create(request: CreateTodoItem.Request): CreateTodoItem.Response {
        TODO("Not yet implemented")
    }

}
