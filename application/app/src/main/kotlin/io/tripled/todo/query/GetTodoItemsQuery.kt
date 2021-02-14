package io.tripled.todo.query

import io.tripled.todo.domain.TodoItems

class GetTodoItemsQuery(private val todoItems: TodoItems) : GetTodoItems {
    override fun getAll() = GetTodoItems.Response(
        todoItems.getAll()
        .map { snapshot ->
            GetTodoItems.Response.TodoItem(
                snapshot.id,
                snapshot.title,
                snapshot.description,
                snapshot.assignee,
            )
        }.toList()
    )
}
