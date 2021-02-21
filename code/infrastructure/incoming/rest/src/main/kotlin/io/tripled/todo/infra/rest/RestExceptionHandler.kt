package io.tripled.todo.infra.rest

import io.tripled.todo.ValidationException
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class RestExceptionHandler {

    @ExceptionHandler(value = [ValidationException::class])
    protected fun handleConflict(ex: ValidationException)
        = ResponseEntity.badRequest().body(ex.validations)
}