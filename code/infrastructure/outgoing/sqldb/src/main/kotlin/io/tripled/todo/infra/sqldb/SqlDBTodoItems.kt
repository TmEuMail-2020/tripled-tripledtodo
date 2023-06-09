package io.tripled.todo.infra.sqldb

import io.tripled.todo.TodoId
import io.tripled.todo.TodoItemStatus
import io.tripled.todo.UserId
import io.tripled.todo.domain.TodoItem
import io.tripled.todo.domain.TodoItems
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import javax.sql.DataSource

class SqlDBTodoItems(dataSource: DataSource,
                     private val creator: (TodoItem.Snapshot) -> TodoItem) : TodoItems {
    init {
        Database.connect(dataSource)
    }

    object TodoitemsTable : Table() {
        private const val idLength = 32
        private const val titleLength = 128
        private const val descriptionLength = 4096
        private const val statusLength = 128

        val todoId = varchar("todo_id", idLength)
        val title = varchar("title", length = titleLength)
        val description = varchar("description", length = descriptionLength)
        val status = varchar("status", length = statusLength)
        val userId = varchar("user_id", idLength)
    }

    override fun save(todoItem: TodoItem) {
        transaction {
            TodoitemsTable.insert { table ->
                val snapshot = todoItem.snapshot
                table[todoId] = snapshot.id.id
                table[title] = snapshot.title
                table[description] = snapshot.description
                table[status] = snapshot.status.name

                snapshot.assignee.let {
                    table[userId] = it!!.id
                }
            }
        }
    }

    override fun find(todoId: TodoId) = transaction {
        TodoitemsTable.select {
            TodoitemsTable.todoId.eq(todoId.id)
        }.toList()[0]
            .let(::rowToSnapshot)
            .let { s -> creator(s) }
    }

    override fun getAll() = transaction {
        TodoitemsTable.selectAll()
            .map(::rowToSnapshot)
            .toList()
    }

    private fun rowToSnapshot(result: ResultRow)
        =  TodoItem.Snapshot(
                TodoId.existing(result[TodoitemsTable.todoId]),
                result[TodoitemsTable.title],
                result[TodoitemsTable.description],
                TodoItemStatus.valueOf(result[TodoitemsTable.status]),
                UserId.existing(result[TodoitemsTable.userId]),
            )
}
