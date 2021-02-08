package io.tripled.todo.fakes

import io.tripled.todo.domain.TodoItem

interface TestTodoItems {
    val lastSaved: TodoItem.Snapshot
    var assumeExisting: TodoItem.Snapshot
}