package io.tripled.todo

import java.util.*

class TodoItem(private val title: String,
               private val description: String) {
    val id: String = UUID.randomUUID().toString()

    data class Snapshot(val id: String, val title: String, val description: String)

    val snapshot: Snapshot
        get() = Snapshot(id, title, description)

}
