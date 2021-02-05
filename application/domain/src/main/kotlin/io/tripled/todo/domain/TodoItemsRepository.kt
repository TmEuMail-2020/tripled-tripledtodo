package io.tripled.todo.domain

interface TodoItemsRepository {
    fun save(createdTodoItem: TodoItem)

}
