package io.tripled.todo

import io.tripled.todo.domain.TodoItem

class TodoItemFactory(private val eventDispatcher: (Any) -> Unit) {
    fun create(snapshot: TodoItem.Snapshot)
        = TodoItem.restoreState(snapshot, eventDispatcher::invoke)
}
