package io.tripled.todo.domain

interface Events {
    fun dispatchEvent(event: Any)
}
