package io.tripled.todo.domain

import io.tripled.todo.DomainException
import io.tripled.todo.TodoId
import io.tripled.todo.UserId
import io.tripled.todo.domain.TodoItem.Status.*

class TodoItem private constructor(snapshot: Snapshot) {
    companion object Factory {
        fun createNew(title: String, description: String)
            = TodoItem(Snapshot(title = title,
                                description = description,
                                id = TodoId.newId(),
                                status = CREATED
        ))
        fun restoreState(snapshot: Snapshot) = TodoItem(snapshot)
    }

    val id: TodoId = snapshot.id
    private var title: String = snapshot.title
    private var description: String = snapshot.description
    private var status = snapshot.status
    private var assignee = snapshot.assignee

    fun finish() {
        status = FINISHED
    }

    fun cancel() {
        if (status == FINISHED){
            throw DomainException("Can't cancel finished todoItem '${this.id.id}'")
        }
        status = CANCELLED
    }

    fun assign(newAssignee: UserId, userExists: (UserId) -> Boolean) {
        if (!userExists.invoke(newAssignee)){
            throw DomainException("Can't assign todoItem '${this.id.id}' to a non-existing user '${newAssignee.id}'")
        }
        assignee = newAssignee
    }

    fun updateInformation(title: String,
                          description: String) {
        this.title = title
        this.description = description
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
