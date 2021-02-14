package io.tripled.todo.fakes

import io.tripled.todo.TodoId
import io.tripled.todo.domain.TodoItem
import io.tripled.todo.domain.TodoItems

class FakeTodoItems : TestTodoItems, TodoItems {

    // ##### test api for consumption by test setup ##### //
    private lateinit var _assumeExisting: TodoItem.Snapshot
    private lateinit var _lastSaved: TodoItem.Snapshot
    private lateinit var _assumeMultipleExisting: List<TodoItem.Snapshot>

    override val lastSaved: TodoItem.Snapshot
        get() = _lastSaved
    override var assumeExisting: TodoItem.Snapshot
        get() = TODO("Not yet implemented")
        set(value) {
            _assumeExisting = value
        }

    override var assumeMultipleExisting: List<TodoItem.Snapshot>
        get() = TODO("Not yet implemented")
        set(value) {
            _assumeMultipleExisting = value
        }

    // ##### fake for consumption by prd code ##### //
    override fun save(todoItem: TodoItem) {
        _lastSaved = todoItem.snapshot
    }

    override fun find(todoId: TodoId) = TodoItem.restoreState(_assumeExisting)
    override fun getAll() = _assumeMultipleExisting
}