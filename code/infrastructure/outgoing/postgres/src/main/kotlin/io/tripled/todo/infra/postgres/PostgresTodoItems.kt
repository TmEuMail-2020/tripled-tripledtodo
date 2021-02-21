package io.tripled.todo.infra.postgres

import io.tripled.todo.TodoId
import io.tripled.todo.TodoItemStatus
import io.tripled.todo.UserId
import io.tripled.todo.domain.TodoItem
import io.tripled.todo.domain.TodoItems
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import javax.sql.DataSource

class PostgresTodoItems(private val dataSource: DataSource) : TodoItems {
    init {
        Database.connect(dataSource)
    }

    object TodoitemsTable : Table() {
        val todoId = varchar("todo_id", 32)
        val title = varchar("title", length = 128)
        val description = varchar("description", length = 4096)
        val status = varchar("status", length = 128)
        val userId = varchar("user_id", 32)

        override val primaryKey = PrimaryKey(todoId, name = "pk_todo_id")
    }

    override fun save(todoItem: TodoItem) {
        TODO("Not yet implemented")
    }

    override fun find(todoId: TodoId): TodoItem? {
        return transaction {
            TodoitemsTable.select {
                TodoitemsTable.todoId.eq(todoId.id)
            }.toList()[0]
                .let { result ->
                    TodoItem.Snapshot(
                        TodoId.existing(result[TodoitemsTable.todoId]),
                        result[TodoitemsTable.title],
                        result[TodoitemsTable.description],
                        TodoItemStatus.valueOf(result[TodoitemsTable.status]),
                        UserId.existing(result[TodoitemsTable.userId]),
                    )
                }
                .let { s -> TodoItem.restoreState(s) }
        }
    }



    override fun getAll(): List<TodoItem.Snapshot> {
        TODO("Not yet implemented")
    }

}
