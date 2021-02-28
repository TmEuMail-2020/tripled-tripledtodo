package io.tripled.todo.fakes

import io.tripled.todo.TodoId
import io.tripled.todo.domain.TodoItem
import io.tripled.todo.domain.TodoItems

class FakeTodoItems : TestTodoItems, TodoItems {

    // ##### test api for consumption by test setup ##### //
    private lateinit var _assumeExisting: TodoItem.Snapshot
    private lateinit var _lastSaved: TodoItem.Snapshot
    private lateinit var _assumeMultipleExisting: List<TodoItem.Snapshot>
    private var _dispatchedEvents: MutableList<Any> = mutableListOf()

    override val dispatchedEvents: List<Any>
        get() = clearWhenReading()

    private fun clearWhenReading(): List<Any> {
        val result = _dispatchedEvents.toList()
        _dispatchedEvents = mutableListOf()
        return result
    }

    override val lastSaved: TodoItem.Snapshot
        get() = _lastSaved
    override var assumeExisting: TodoItem.Snapshot
        get() = TODO("Not yet implemented")
        set(value) {
            _assumeExisting = value
        }

    fun dispatchEvent(event: Any)
        = _dispatchedEvents.add(event)

    override var assumeMultipleExisting: List<TodoItem.Snapshot>
        get() = TODO("Not yet implemented")
        set(value) {
            _assumeMultipleExisting = value
        }

    // ##### fake for consumption by prd code ##### //
    override fun save(todoItem: TodoItem) {
        _lastSaved = todoItem.snapshot
    }

    override fun find(todoId: TodoId) = TodoItem.restoreState(_assumeExisting, ::dispatchEvent)
    override fun getAll() = _assumeMultipleExisting
}