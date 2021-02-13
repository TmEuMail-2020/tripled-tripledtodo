package io.tripled.todo

import io.tripled.todo.domain.TodoItemsRepository

class CancelTodoItemCommand(private val todoItemRepository: TodoItemsRepository) : CancelTodoItem {
    override fun cancel(request: CancelTodoItem.Request) {
        val todoItem = todoItemRepository.find(request.todoId)!!
        todoItem.cancel()
        todoItemRepository.save(todoItem)
    }

}
