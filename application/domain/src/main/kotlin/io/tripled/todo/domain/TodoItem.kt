package io.tripled.todo.domain

import io.tripled.todo.DomainException
import io.tripled.todo.TodoId
import io.tripled.todo.TodoItemStatus
import io.tripled.todo.UserId
import io.tripled.todo.TodoItemStatus.*

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
        if (status in listOf(FINISHED, CANCELLED)){
            throw DomainException("Can't change todoItem '${id.id}' it's '${status.name}'")
        }

        this.title = title
        this.description = description
    }

    data class Snapshot(val id: TodoId,
                        val title: String,
                        val description: String,
                        val status: TodoItemStatus,
                        val assignee: UserId? = null)
    val snapshot: Snapshot
        get() = Snapshot(id, title,
                         description, status,
                         assignee)
}
