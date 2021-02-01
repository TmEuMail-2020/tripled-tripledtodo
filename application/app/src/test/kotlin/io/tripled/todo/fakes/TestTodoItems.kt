package io.tripled.todo.fakes

import io.tripled.todo.TodoItem

interface TestTodoItems {
    val lastSaved: TodoItem.Snapshot
}