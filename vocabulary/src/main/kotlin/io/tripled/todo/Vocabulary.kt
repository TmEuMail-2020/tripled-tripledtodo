package io.tripled.todo

import java.util.*

data class TodoId(val id: String){
    companion object Factory {
        fun newId(): TodoId = TodoId(UUID.randomUUID().toString())
    }
}