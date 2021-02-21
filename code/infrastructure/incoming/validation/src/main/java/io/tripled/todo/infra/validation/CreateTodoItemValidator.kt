package io.tripled.todo.infra.validation

import io.tripled.todo.command.CreateTodoItem

class CreateTodoItemValidator(private val delegatee: CreateTodoItem) : CreateTodoItem {
    override fun create(request: CreateTodoItem.Request): CreateTodoItem.Response {
        val validations = mutableListOf<ValidationException.Validation>()
        if (request.title.isEmpty()){
            validations += ValidationException.Validation("title", "should not be empty")
        }
        if (request.description.isEmpty()){
            validations += ValidationException.Validation("description", "should not be empty")
        }
        if (validations.isNotEmpty()){
            throw ValidationException(*validations.toTypedArray())
        }

        return delegatee.create(request)
    }

}
