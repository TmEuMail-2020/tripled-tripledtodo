package io.tripled.todo.infra.validation

import io.tripled.todo.ValidationException
import io.tripled.todo.command.CreateTodoItem

class CreateTodoItemValidator(private val delegatee: CreateTodoItem) : CreateTodoItem {
    override fun create(request: CreateTodoItem.Request): CreateTodoItem.Response {
        validate(request)

        return delegatee.create(request)
    }

    private fun validate(request: CreateTodoItem.Request) {
        val validations = mutableListOf<ValidationException.Validation>()
        validateTitle(request, validations)
        validateDescription(request, validations)
        throwIfViolations(validations)
    }

    private fun validateDescription(
        request: CreateTodoItem.Request,
        validations: MutableList<ValidationException.Validation>
    ) {
        if (request.description.isEmpty()) {
            validations += ValidationException.Validation("description", "should not be empty")
        }
    }

    private fun validateTitle(
        request: CreateTodoItem.Request,
        validations: MutableList<ValidationException.Validation>
    ) {
        if (request.title.isEmpty()) {
            validations += ValidationException.Validation("title", "should not be empty")
        }
    }

    private fun throwIfViolations(validations: MutableList<ValidationException.Validation>) {
        if (validations.isNotEmpty()) {
            throw ValidationException(validations)
        }
    }
}
