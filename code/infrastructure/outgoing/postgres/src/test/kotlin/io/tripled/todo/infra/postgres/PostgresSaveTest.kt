package io.tripled.todo.infra.postgres

import io.zonky.test.db.postgres.embedded.LiquibasePreparer
import io.zonky.test.db.postgres.embedded.PreparedDbProvider

import io.zonky.test.db.postgres.embedded.DatabasePreparer
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

import org.springframework.test.context.junit.jupiter.SpringExtension
import javax.sql.DataSource


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
    }
}