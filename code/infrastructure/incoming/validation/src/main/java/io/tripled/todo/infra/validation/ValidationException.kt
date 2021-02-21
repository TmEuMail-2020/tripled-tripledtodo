package io.tripled.todo.infra.validation

class ValidationException(val validations: List<Validation>) : RuntimeException() {
    data class Validation(val field: String, val message: String)
}
