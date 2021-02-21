package io.tripled.todo.infra.validation

class ValidationException(vararg val validations: Validation) : RuntimeException() {

    data class Validation(val field: String, val message: String)

    override fun equals(other: Any?): Boolean {
        if (other != null && other is ValidationException){
            return validations.contentEquals(other.validations)
        }
        return false
    }
}
