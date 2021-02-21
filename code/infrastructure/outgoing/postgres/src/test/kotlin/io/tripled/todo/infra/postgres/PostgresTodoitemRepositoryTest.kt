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
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired

import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate

import org.springframework.test.context.junit.jupiter.SpringExtension
import javax.sql.DataSource
import java.util.HashMap
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource

import org.springframework.jdbc.core.namedparam.SqlParameterSource




@ExtendWith(SpringExtension::class)
@SpringBootTest(classes = [PostgresTodoitemRepositoryTest.PostgresTestDatabase::class])
internal class PostgresTodoitemRepositoryTest {

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
    lateinit var jdbcTemplate: NamedParameterJdbcTemplate
    @Autowired
    lateinit var todoItems: TodoItems

    @BeforeEach
    internal fun setUp() {
        jdbcTemplate.update("delete from todoitems", mapOf<String, Any>())
    }

    @Test
    fun findTest(){
        // given
        val namedParameters = MapSqlParameterSource()
            .addValue("todo_id", "todo-123")
            .addValue("title", "a title")
            .addValue("description", "a description")
            .addValue("status", "CREATED")
            .addValue("user_id", "a user")

        jdbcTemplate.update(
            """INSERT INTO todoitems (todo_id, title, description, status, user_id) 
               VALUES (:todo_id, :title, :description, :status, :user_id)""",
            namedParameters
        )

        // when
        val result = todoItems.find(TodoId.existing("todo-123"))

        // then
        assertThat(result!!.snapshot).isEqualTo(TodoItem.Snapshot(
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
        fun jdbcTemplate(dataSource: DataSource) = NamedParameterJdbcTemplate(dataSource)

        @Bean
        fun todoItems(dataSource: DataSource) = PostgresTodoItems(dataSource)
    }
}