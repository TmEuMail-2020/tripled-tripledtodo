package io.tripled.todo.domain

import io.tripled.todo.TodoItem

interface TodoItemsRepository {
    fun save(createdTodoItem: TodoItem)

}
