package io.tripled.todo.mothers

import io.tripled.todo.TodoId
import io.tripled.todo.domain.TodoItem

class Todos {
    companion object {
        val paintingTheRoom = TodoItem.Snapshot(
            TodoId.existing("abc-123"),
            "Painting",
            "Paint living room white",
            TodoItem.Status.CREATED,
        )

        val cancelledPaintingTheRoom = paintingTheRoom.copy(status = TodoItem.Status.CANCELLED)
        val finishedPaintingTheRoom  = paintingTheRoom.copy(status = TodoItem.Status.FINISHED)
    }
}