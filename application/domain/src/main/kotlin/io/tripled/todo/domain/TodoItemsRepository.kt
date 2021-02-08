package io.tripled.todo.domain

import io.tripled.todo.TodoId

interface TodoItemsRepository {
    fun save(todoItem: TodoItem)
    fun find(todoId: TodoId): TodoItem?
}
