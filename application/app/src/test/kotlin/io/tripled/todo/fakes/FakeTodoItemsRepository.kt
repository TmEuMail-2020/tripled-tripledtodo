package io.tripled.todo.fakes

import io.tripled.todo.TodoId
import io.tripled.todo.domain.TodoItem
import io.tripled.todo.domain.TodoItemsRepository

class FakeTodoItemsRepository : TestTodoItems, TodoItemsRepository {
    private lateinit var _assumeExisting: TodoItem.Snapshot
    private lateinit var _lastSaved: TodoItem.Snapshot

    override val lastSaved: TodoItem.Snapshot
        get() = _lastSaved
    override var assumeExisting: TodoItem.Snapshot
        get() = TODO("Not yet implemented")
        set(value) {
            _assumeExisting = value
        }

    override fun save(todoItem: TodoItem) {
        _lastSaved = todoItem.snapshot
    }

    override fun find(todoId: TodoId) = TodoItem.restoreState(_assumeExisting)
}