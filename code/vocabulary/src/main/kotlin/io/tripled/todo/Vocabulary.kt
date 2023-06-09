package io.tripled.todo

import java.util.*

data class TodoId(val id: String){
    companion object Factory {
        fun newId(): TodoId = TodoId(UUID.randomUUID().toString())
        fun existing(originalId: String) = TodoId(originalId)
    }
}

enum class TodoItemStatus {
    CREATED,
    ASSIGNED,
    FINISHED,
    CANCELLED,
}

data class UserId(val id: String){
    companion object Factory {
        fun existing(originalId: String) = UserId(originalId)
    }
}
