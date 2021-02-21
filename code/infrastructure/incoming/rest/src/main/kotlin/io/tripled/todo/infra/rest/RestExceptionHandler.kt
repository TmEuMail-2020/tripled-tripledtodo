package io.tripled.todo.infra.rest

import io.tripled.todo.DomainException
import io.tripled.todo.ValidationException
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class RestExceptionHandler {

    @ExceptionHandler(value = [ValidationException::class])
    fun handleValidationException(ex: ValidationException)
        = ResponseEntity.badRequest().body(ex.validations)


    @ExceptionHandler(value = [DomainException::class])
    fun handleDomainException(ex: DomainException)
            = ResponseEntity.badRequest().body(DomainExceptionResponse(ex.message!!))

    data class DomainExceptionResponse(val message: String)
}