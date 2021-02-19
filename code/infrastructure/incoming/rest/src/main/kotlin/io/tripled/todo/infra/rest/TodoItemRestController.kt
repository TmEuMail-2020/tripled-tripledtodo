package io.tripled.todo.infra.rest

import io.tripled.todo.TodoId
import io.tripled.todo.command.CancelTodoItem
import io.tripled.todo.command.CreateTodoItem
import io.tripled.todo.command.FinishTodoItem
import io.tripled.todo.command.UpdateInformationInTodoItem
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
class TodoItemRestController(
    private val createTodoItem: CreateTodoItem,
    private val cancelTodoItem: CancelTodoItem,
    private val finishTodoItem: FinishTodoItem,
    private val updateInformationInTodoItem: UpdateInformationInTodoItem,
) {

    @PostMapping("/api/todo")
    @ResponseStatus(HttpStatus.CREATED)
    fun createTodoItem(@RequestBody request: CreateTodoItem.Request)
        = createTodoItem.create(request)

    @PutMapping("/api/todo/{todoId}/information")
    fun updateInformationInTodoItem(@PathVariable("todoId") todoId: TodoId,
                                    @RequestBody restRequest: UpdateInformationRequest)
        = updateInformationInTodoItem.updateInformation(
                                        UpdateInformationInTodoItem.Request(
                                            todoId,
                                            restRequest.title,
                                            restRequest.description,
                                        ))

    data class UpdateInformationRequest(val title: String,
                                        val description: String)

    @PutMapping("/api/todo/{todoId}/status")
    fun updateStatus(@PathVariable("todoId") todoId: TodoId,
                     @RequestBody request: UpdateStatusRequest)
        = ResponseEntity<Unit>(
        when (request.value) {
            "cancel" -> {
                cancelTodoItem.cancel(CancelTodoItem.Request(todoId))
                HttpStatus.OK
            }
            "finish" -> {
                finishTodoItem.finish(FinishTodoItem.Request(todoId))
                HttpStatus.OK
            }
            else -> HttpStatus.BAD_REQUEST
        })

    data class UpdateStatusRequest(val value: String)

}