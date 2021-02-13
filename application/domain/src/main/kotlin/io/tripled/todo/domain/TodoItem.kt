package io.tripled.todo.domain

import io.tripled.todo.TodoId
import io.tripled.todo.UserId

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
    private var assignee = snapshot.assignee

    fun finish() {
        status = Status.FINISHED
    }

    fun cancel() {
        status = Status.CANCELLED
    }

    fun assign(newAssignee: UserId) {
        assignee = newAssignee
    }

    enum class Status {
        CREATED,
        FINISHED,
        CANCELLED
    }

    data class Snapshot(val id: TodoId,
                        val title: String,
                        val description: String,
                        val status: Status,
                        val assignee: UserId? = null)
    val snapshot: Snapshot
        get() = Snapshot(id, title,
                         description, status,
                         assignee)
}
