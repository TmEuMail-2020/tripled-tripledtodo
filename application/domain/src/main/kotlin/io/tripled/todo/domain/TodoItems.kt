package io.tripled.todo.domain

import io.tripled.todo.TodoId

interface TodoItems {
    fun save(todoItem: TodoItem)
    fun find(todoId: TodoId): TodoItem?
}
