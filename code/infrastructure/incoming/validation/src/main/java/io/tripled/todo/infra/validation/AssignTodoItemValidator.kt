package io.tripled.todo.infra.validation

import io.tripled.todo.command.AssignTodoItem
import org.springframework.validation.Validator

class AssignTodoItemValidator(private val delegatee: AssignTodoItem,
                              private val validator: Validator) : AssignTodoItem {

}




