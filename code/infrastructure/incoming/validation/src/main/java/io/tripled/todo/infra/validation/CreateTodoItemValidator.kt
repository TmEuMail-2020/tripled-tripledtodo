package io.tripled.todo.infra.validation

import io.tripled.todo.command.CreateTodoItem

class CreateTodoItemValidator : CreateTodoItem {
    override fun create(request: CreateTodoItem.Request): CreateTodoItem.Response {
        val validations = mutableListOf<ValidationException.Validation>()
        if (request.title.isEmpty()){
            validations += ValidationException.Validation("title", "should not be empty")
        }
        if (request.description.isEmpty()){
            validations += ValidationException.Validation("description", "should not be empty")
        }
        throw ValidationException(*validations.toTypedArray())
    }

}
