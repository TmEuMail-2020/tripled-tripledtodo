package io.tripled.todo.infra.rest

import io.tripled.todo.TodoId
import io.tripled.todo.UserId
import io.tripled.todo.command.AssignTodoItem
import io.tripled.todo.command.CancelTodoItem
import io.tripled.todo.command.CreateTodoItem
import io.tripled.todo.command.FinishTodoItem
import io.tripled.todo.command.UpdateInformationInTodoItem
import io.tripled.todo.query.GetTodoItem
import io.tripled.todo.query.GetTodoItems
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
class TodoItemRestQueryController(
    private val getTodoItem: GetTodoItem,
    private val getTodoItems: GetTodoItems,
){
    @GetMapping("/api/todo/{todoId}")
    fun getTodoItem(@PathVariable("todoId") todoId: TodoId)
            = getTodoItem.get(GetTodoItem.Request(todoId)).todoItem

    @GetMapping("/api/todo/")
    fun getTodoItems()
            = getTodoItems.getAll().todoItems
}

@RestController
class TodoItemRestCommandController(
    private val createTodoItem: CreateTodoItem,
    private val cancelTodoItem: CancelTodoItem,
    private val finishTodoItem: FinishTodoItem,
    private val updateInformationInTodoItem: UpdateInformationInTodoItem,
    private val assignTodoItem: AssignTodoItem,
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

    @PutMapping("/api/todo/{todoId}/assignee")
    fun assignTodoItem(@PathVariable("todoId") todoId: TodoId,
                       @RequestBody restRequest: AssignUser)
        = assignTodoItem.assign(AssignTodoItem.Request(
                                    todoId,
                                    restRequest.user,
                                ))

    data class AssignUser(val user: UserId)

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
