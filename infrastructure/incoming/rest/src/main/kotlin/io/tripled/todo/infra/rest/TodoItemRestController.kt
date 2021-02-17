package io.tripled.todo.infra.rest

import io.tripled.todo.command.CreateTodoItem
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
class TodoItemRestController(
    private val createTodoItem: CreateTodoItem
) {

    @PostMapping("/api/todo")
    @ResponseStatus( HttpStatus.CREATED )
    fun createTodoItem(@RequestBody request: CreateTodoItem.Request) = createTodoItem.create(request)

}