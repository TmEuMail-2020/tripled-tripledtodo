package io.tripled.todo.mothers

import io.tripled.todo.TodoId
import io.tripled.todo.TodoItemStatus
import io.tripled.todo.domain.TodoItem

class Todos {
    companion object {
        val paintingTheRoom = TodoItem.Snapshot(
            TodoId.existing("abc-123"),
            "Painting",
            "Paint living room white",
            TodoItemStatus.CREATED,
        )


        val cancelledPaintingTheRoom = paintingTheRoom.copy(status = TodoItemStatus.CANCELLED)
        val finishedPaintingTheRoom  = paintingTheRoom.copy(status = TodoItemStatus.FINISHED)

        val cleaningTheGutter = TodoItem.Snapshot(
            TodoId.existing("xyz-987"),
            "Cleaning gutter",
            "Gutters need to be cleaned on the front and back side of the house",
            TodoItemStatus.CREATED,
        )
    }
}
