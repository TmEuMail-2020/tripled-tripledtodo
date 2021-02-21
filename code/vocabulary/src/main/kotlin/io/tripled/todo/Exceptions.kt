package io.tripled.todo

class DomainException(message: String) : RuntimeException(message)

class ValidationException(val validations: List<Validation>) : RuntimeException() {
    data class Validation(val field: String, val message: String)
}
