package io.tripled.todo.infra.validation

import io.tripled.todo.command.AssignTodoItem
import javax.validation.constraints.NotBlank

class AssignTodoItemValidator(private val delegatee: AssignTodoItem,
                              private val validator: javax.validation.Validator
) : AssignTodoItem {

    data class ValidationContract(
        @field:NotBlank private val todoId: String?,
    )

    override fun assign(request: AssignTodoItem.Request) {
        val validationTarget = ValidationContract(
            request.todoId.id,
        )
        val results = validator.validate(validationTarget)

        if (results.isNotEmpty()){
            throw ValidationException(
                results
                    .map { fe -> ValidationException.Validation(fe.propertyPath.toString(), fe.message) }
                    .toList()
            )
        }
        delegatee.assign(request)
    }

}




