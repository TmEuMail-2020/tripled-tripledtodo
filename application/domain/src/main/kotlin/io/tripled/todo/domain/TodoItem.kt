package io.tripled.todo.domain

import io.tripled.todo.TodoId

class TodoItem(private val title: String,
               private val description: String) {
    val id: TodoId = TodoId.newId()

    data class Snapshot(val id: TodoId, val title: String, val description: String)

    val snapshot: Snapshot
        get() = Snapshot(id, title, description)

}
