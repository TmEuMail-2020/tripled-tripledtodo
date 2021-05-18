package io.tripled.todo.infra.postgres

import io.tripled.todo.TodoId
import io.tripled.todo.TodoItemStatus
import io.tripled.todo.UserId
import io.tripled.todo.domain.TodoItem
import io.tripled.todo.domain.TodoItems
import io.zonky.test.db.postgres.embedded.DatabasePreparer
import io.zonky.test.db.postgres.embedded.LiquibasePreparer
import io.zonky.test.db.postgres.embedded.PreparedDbProvider
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.sql.ResultSet
import javax.sql.DataSource


@ExtendWith(SpringExtension::class)
@SpringBootTest(classes = [PostgresTodoitemRepositoryTest.PostgresTestDatabase::class])
internal class PostgresTodoitemRepositoryTest {
    @Autowired
    lateinit var jdbcTemplate: NamedParameterJdbcTemplate
    @Autowired
    lateinit var todoItems: TodoItems

    @BeforeEach
    internal fun setUp() {
        jdbcTemplate.update("delete from todoitems", mapOf<String, Any>())
    }

    @Test
    fun saveTest(){
        // given
        val newTodoItem = TodoItem.restoreState(
            TodoItem.Snapshot(
                TodoId.existing("todo-123"),
                "a title",
                "a description",
                TodoItemStatus.CREATED,
                UserId.existing("a user"),
            )
        ) { }

        // when
        todoItems.save(newTodoItem)

        // then
        val query = jdbcTemplate.query<Any>(
            "select * from todoitems where todo_id = 'todo-123'"
        ) { rs: ResultSet, _: Int ->
            TodoItem.Snapshot(
                TodoId.existing(rs.getString("todo_id")),
                rs.getString("title"),
                rs.getString("description"),
                TodoItemStatus.valueOf(rs.getString("status")),
                UserId.existing(rs.getString("user_id")),
            )
        }.toList()

        assertThat(query.size).isEqualTo(1)
        assertThat(query[0]).isEqualTo(TodoItem.Snapshot(
            TodoId.existing("todo-123"),
            "a title",
            "a description",
            TodoItemStatus.CREATED,
            UserId.existing("a user"),
        ))
    }


    @Test
    fun findTest(){
        // given
        insertTestRecord("todo-123", "a title", "a description", "CREATED", "a user")

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
    fun getAllTest(){
        // given
        insertTestRecord("todo-123", "a title1", "a description1", "CREATED", "a user1")
        insertTestRecord("todo-456", "a title2", "a description2", "FINISHED", "a user2")

        // when
        val result = todoItems.getAll()

        // then
        assertThat(result[0]).isEqualTo(TodoItem.Snapshot(
            TodoId.existing("todo-123"),
            "a title1",
            "a description1",
            TodoItemStatus.CREATED,
            UserId.existing("a user1"),
        ))
        assertThat(result[1]).isEqualTo(TodoItem.Snapshot(
            TodoId.existing("todo-456"),
            "a title2",
            "a description2",
            TodoItemStatus.FINISHED,
            UserId.existing("a user2"),
        ))
    }

    private fun insertTestRecord(todoId: String, title: String, description: String, status: String, userId: String) {
        val namedParameters = MapSqlParameterSource()
            .addValue("todo_id", todoId)
            .addValue("title", title)
            .addValue("description", description)
            .addValue("status", status)
            .addValue("user_id", userId)

        jdbcTemplate.update(
            """INSERT INTO todoitems (todo_id, title, description, status, user_id) 
                   VALUES (:todo_id, :title, :description, :status, :user_id)""",
            namedParameters
        )
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
        fun todoItems(dataSource: DataSource) = PostgresTodoItems(dataSource){
            TodoItem.restoreState(it) {}
        }
    }
}
