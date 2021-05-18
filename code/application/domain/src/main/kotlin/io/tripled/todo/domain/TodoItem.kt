package io.tripled.todo.domain

import io.tripled.todo.DomainException
import io.tripled.todo.TodoId
import io.tripled.todo.TodoItemStatus
import io.tripled.todo.TodoItemStatus.ASSIGNED
import io.tripled.todo.TodoItemStatus.CANCELLED
import io.tripled.todo.TodoItemStatus.CREATED
import io.tripled.todo.TodoItemStatus.FINISHED
import io.tripled.todo.UserId

class TodoItem private constructor(snapshot: Snapshot,
                                   private val dispatch: (Any) -> Unit) {
    companion object Factory {
        fun createNew(title: String, description: String, dispatchEvent: (Any) -> Unit)
            = TodoItem(title,
                        description,
                        TodoId.newId(),
                        CREATED,
                        dispatchEvent
                    )
        fun restoreState(snapshot: Snapshot, dispatchEvent: (Any) -> Unit) = TodoItem(snapshot, dispatchEvent)
    }

    constructor(title: String,
                description: String,
                id: TodoId, status:
                TodoItemStatus,
                dispatch: (Any) -> Unit) : this(
        Snapshot(title = title,
            description = description,
            id = id,
            status = CREATED
        ), dispatch){
                dispatch(TodoItemCreated(id,
                title,
                description,
                status))
            }

    data class TodoItemCreated(val id: TodoId,
                               val title: String,
                               val description: String,
                               val status: TodoItemStatus)

    data class TodoItemCancelled(val id: TodoId,
                                 val status: TodoItemStatus)

    data class TodoItemAssigned(val id: TodoId,
                                val status: TodoItemStatus,
                                val assignee: UserId?)

    data class TodoItemInformationUpdated(val id: TodoId,
                                val title: String,
                                val description: String)

    val id = snapshot.id
    private var title = snapshot.title
    private var description = snapshot.description
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
        dispatch(TodoItemCancelled(id, status))
    }

    fun assign(newAssignee: UserId, userExists: (UserId) -> Boolean) {
        if (!userExists(newAssignee)){
            throw DomainException("Can't assign todoItem '${this.id.id}' to a non-existing user '${newAssignee.id}'")
        }
        assignee = newAssignee
        status = ASSIGNED

        dispatch(TodoItemAssigned(id, status, assignee))
    }

    fun updateInformation(title: String,
                          description: String) {
        if (status in listOf(FINISHED, CANCELLED)){
            throw DomainException("Can't change todoItem '${id.id}' it's '${status.name}'")
        }

        this.title = title
        this.description = description

        dispatch(TodoItemInformationUpdated(id, title, description))
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
