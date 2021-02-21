package io.tripled.todo.infra.validation

import io.tripled.todo.command.AssignTodoItem
import javax.validation.ConstraintViolation
import javax.validation.constraints.NotBlank

class AssignTodoItemValidator(
        private val delegatee: AssignTodoItem,
        private val validator: javax.validation.Validator
    ) : AssignTodoItem {

    data class ValidationContract(
        @field:NotBlank private val todoId: String?,
    )

    override fun assign(request: AssignTodoItem.Request) {
        validate(request)
        delegatee.assign(request)
    }

    private fun validate(request: AssignTodoItem.Request) {
        val validationTarget = copyValidationFields(request)
        val results = validator.validate(validationTarget)
        throwValidations(results)
    }

    private fun throwValidations(results: Set<ConstraintViolation<ValidationContract>>) {
        if (results.isNotEmpty()) {
            throw ValidationException(
                results
                    .map { fe -> ValidationException.Validation(fe.propertyPath.toString(), fe.message) }
                    .toList()
            )
        }
    }

    private fun copyValidationFields(request: AssignTodoItem.Request): ValidationContract = ValidationContract(
        request.todoId.id,
    )

}




