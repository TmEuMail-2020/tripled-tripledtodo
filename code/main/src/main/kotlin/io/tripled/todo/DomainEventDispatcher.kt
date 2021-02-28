package io.tripled.todo

import io.tripled.todo.domain.Events

class DomainEventDispatcher(private val internalEvents: MutableList<Any> = mutableListOf()) : Events {

    val events: List<Any>
        get() = internalEvents.toList()

    override fun dispatchEvent(event: Any){
        internalEvents.add(event)
    }

}
