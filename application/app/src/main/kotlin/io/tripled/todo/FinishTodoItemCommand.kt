package io.tripled.todo

import io.tripled.todo.domain.TodoItemsRepository

class FinishTodoItemCommand(private val todoItemRepository: TodoItemsRepository) : FinishTodoItem {
    override fun create(request: FinishTodoItem.Request) {
        val todoItem = todoItemRepository.find(request.todoId)!!
        todoItem.finish()
        todoItemRepository.save(todoItem)
    }

}
