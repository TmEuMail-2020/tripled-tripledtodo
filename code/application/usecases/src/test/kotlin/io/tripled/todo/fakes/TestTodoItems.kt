package io.tripled.todo.fakes

import io.tripled.todo.domain.TodoItem

interface TestTodoItems {
    val dispatchedEvents: List<Any>
    val lastSaved: TodoItem.Snapshot
    var assumeExisting: TodoItem.Snapshot
    var assumeMultipleExisting: List<TodoItem.Snapshot>
}
