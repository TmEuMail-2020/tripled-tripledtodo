package io.tripled.todo.query

import io.tripled.todo.domain.TodoItems

class GetTodoItemQuery(private val todoItems: TodoItems) : GetTodoItem {
    override fun get(request: GetTodoItem.Request) =
        GetTodoItem.Response(
            todoItems.find(request.todoId)
                .let { todoItem -> todoItem!!.snapshot }
                .let { snapshot ->
                    GetTodoItem.Response.TodoItem(
                        snapshot.id,
                        snapshot.title,
                        snapshot.description,
                        snapshot.assignee,
                        snapshot.status,
                    )
                }
        )
}
