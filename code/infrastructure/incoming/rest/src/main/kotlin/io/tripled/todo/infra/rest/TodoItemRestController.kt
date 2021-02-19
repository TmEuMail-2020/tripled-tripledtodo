package io.tripled.todo.infra.rest

import io.tripled.todo.TodoId
import io.tripled.todo.command.CancelTodoItem
import io.tripled.todo.command.CreateTodoItem
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
class TodoItemRestController(
    private val createTodoItem: CreateTodoItem,
    private val cancelTodoItem: CancelTodoItem
) {

    @PostMapping("/api/todo")
    @ResponseStatus( HttpStatus.CREATED )
    fun createTodoItem(@RequestBody request: CreateTodoItem.Request) = createTodoItem.create(request)

    @PutMapping("/api/todo/{todoId}/status")
    fun updateStatus(@PathVariable("todoId") todoId: TodoId,
                     @RequestBody request: UpdateStatusRequest)
        = ResponseEntity<Unit>(
        when (request.value) {
            "cancel" -> {
                cancelTodoItem.cancel(CancelTodoItem.Request(todoId))
                HttpStatus.OK
            }
            else -> HttpStatus.BAD_REQUEST
        })

    data class UpdateStatusRequest(val value: String)

}