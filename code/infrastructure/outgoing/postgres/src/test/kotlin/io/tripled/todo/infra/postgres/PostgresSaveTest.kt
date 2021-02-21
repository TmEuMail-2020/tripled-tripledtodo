package io.tripled.todo.infra.postgres

import io.tripled.todo.TodoId
import io.tripled.todo.TodoItemStatus
import io.tripled.todo.UserId
import io.tripled.todo.domain.TodoItem
import io.tripled.todo.domain.TodoItems
import io.zonky.test.db.postgres.embedded.LiquibasePreparer
import io.zonky.test.db.postgres.embedded.PreparedDbProvider

import io.zonky.test.db.postgres.embedded.DatabasePreparer
import org.assertj.core.api.Assertions.assertThat
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired

import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.core.JdbcTemplate

import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.jdbc.JdbcTestUtils
import javax.sql.DataSource
import java.util.HashMap





@ExtendWith(SpringExtension::class)
@SpringBootTest(classes = [DaoTestUsingJunit5.PostgresTestDatabase::class])
internal class DaoTestUsingJunit5 {

    object TodoitemsTable : Table() {
        val todoId = varchar("todo_id", 32)
        val title = varchar("title", length = 128)
        val description = varchar("description", length = 4096)
        val status = varchar("status", length = 128)
        val userId = varchar("user_id", 32)

        override val primaryKey = PrimaryKey(todoId, name = "pk_todo_id")
    }

    @Autowired
    lateinit var dataSource: DataSource
    @Autowired
    lateinit var jdbcTemplate: JdbcTemplate
    @Autowired
    lateinit var todoItems: TodoItems

    @Test
    fun findTest(){
        // given
        val params: MutableMap<String, Any> = HashMap()
        params["todo_id"] = "todo-123"
        params["title"] = "a title"
        params["description"] = "a description"
        params["status"] = "CREATED"
        params["user_id"] = "a user"

        jdbcTemplate.update(
            """INSERT INTO todoitems (todo_id, title, description, status, user_id) 
               VALUES (:todo_id, :title, :description, :status, :user_id)""",
            params
        )

        // when
        val result = todoItems.find(TodoId.existing("todo-123"))

        // then
        assertThat(result).isEqualTo(TodoItem.Snapshot(
            TodoId.existing("todo-123"),
            "a title",
            "a description",
            TodoItemStatus.CREATED,
            UserId.existing("a user"),
        ))

    }

    @Test
    fun someTest() {
        Database.connect(dataSource)

        transaction {
            TodoitemsTable.insert {
                it[todoId] = "todo-123"
                it[title] = "db title"
                it[description] = "db description"
                it[status] = "CREATED"
                it[userId] = "a user"
            }

            for (todoItem in TodoitemsTable.selectAll()) {
                println("${todoItem[TodoitemsTable.todoId]}: ${todoItem[TodoitemsTable.title]}")
            }
        }
    }

    @Configuration
    internal class PostgresTestDatabase {
        @Bean
        fun datasource(): DataSource {
            val db: DatabasePreparer = LiquibasePreparer.forClasspathLocation("liquibase/master.xml")
            return PreparedDbProvider.forPreparer(db).createDataSource()
        }

        @Bean
        fun jdbcTemplate(dataSource: DataSource) = JdbcTemplate(dataSource)

        @Bean
        fun todoItems(dataSource: DataSource) = PostgresTodoItems(dataSource)
    }
}