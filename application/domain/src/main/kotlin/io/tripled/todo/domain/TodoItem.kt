package io.tripled.todo.domain

import io.tripled.todo.TodoId

class TodoItem private constructor(snapshot: Snapshot) {
    companion object Factory {
        fun createNew(title: String, description: String)
            = TodoItem(Snapshot(title = title,
                                description = description,
                                id = TodoId.newId(),
                                status = Status.CREATED))
        fun restoreState(snapshot: Snapshot) = TodoItem(snapshot)
    }

    val id: TodoId = snapshot.id
    private val title: String = snapshot.title
    private val description: String = snapshot.description
    private var status = snapshot.status

    fun finish() {
        status = Status.FINISHED
    }

    enum class Status {
        CREATED,
        FINISHED
    }

    data class Snapshot(val id: TodoId, val title: String, val description: String, val status: Status)
    val snapshot: Snapshot
        get() = Snapshot(id, title, description, status)
}
