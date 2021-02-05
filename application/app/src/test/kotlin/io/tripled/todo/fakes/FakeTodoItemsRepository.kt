package io.tripled.todo.fakes

import io.tripled.todo.domain.TodoItem
import io.tripled.todo.domain.TodoItemsRepository

class FakeTodoItemsRepository : TestTodoItems, TodoItemsRepository {
    private lateinit var _lastSaved: TodoItem.Snapshot

    override val lastSaved: TodoItem.Snapshot
        get() = _lastSaved

    override fun save(createdTodoItem: TodoItem) {
        _lastSaved = createdTodoItem.snapshot
    }
}