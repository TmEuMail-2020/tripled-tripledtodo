package io.tripled.todo

import io.tripled.todo.command.AssignTodoItem
import io.tripled.todo.command.AssignTodoItemCommand
import io.tripled.todo.command.CancelTodoItemCommand
import io.tripled.todo.command.CreateTodoItem
import io.tripled.todo.command.CreateTodoItemCommand
import io.tripled.todo.command.FinishTodoItemCommand
import io.tripled.todo.command.UpdateInformationInTodoItemCommand
import io.tripled.todo.domain.Events
import io.tripled.todo.domain.TodoItems
import io.tripled.todo.infra.sqldb.SqlDBTodoItems
import io.tripled.todo.infra.validation.AssignTodoItemValidator
import io.tripled.todo.infra.validation.CreateTodoItemValidator
import io.tripled.todo.query.GetTodoItemQuery
import io.tripled.todo.query.GetTodoItemsQuery
import io.tripled.todo.user.UserService
import liquibase.Liquibase
import liquibase.database.jvm.JdbcConnection
import liquibase.resource.ClassLoaderResourceAccessor
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType
import javax.sql.DataSource
import javax.validation.Validator

@SpringBootApplication
class TasksApplication

fun main(vararg args: String) {
	runApplication<TasksApplication>(*args)
}

@Configuration
class UseCases {

	@Bean
	fun createTodoItem(todoItems: TodoItems, eventDispatcher: Events)
		= CrossCuttingConcerns<CreateTodoItem>(
			{createTodoItem ->  CreateTodoItemValidator(createTodoItem) },
			CreateTodoItemCommand(todoItems, eventDispatcher::dispatchEvent)
		).command


	@Bean
	fun cancelTodoItem(todoItems: TodoItems) = CancelTodoItemCommand(todoItems)

	@Bean
	fun finishTodoItem(todoItems: TodoItems) = FinishTodoItemCommand(todoItems)

	@Bean
	fun getTodoItem(todoItems: TodoItems) = GetTodoItemQuery(todoItems)

	@Bean
	fun getTodoItems(todoItems: TodoItems) = GetTodoItemsQuery(todoItems)

	@Bean
	fun assignTodoItem(
		todoItems: TodoItems,
		validator: Validator,
		userService: UserService,
	)
	= CrossCuttingConcerns<AssignTodoItem>(
		{assignTodoItem ->  AssignTodoItemValidator(assignTodoItem, validator) },
		AssignTodoItemCommand(todoItems, userService::exists)
	).command

	@Bean
	fun userService() = UserService(
		listOf("kris", "gert", "yves", "sander", "domenique", "guido")
			.map { user -> UserId.existing(user)}.toList()
	)

	@Bean
	fun updateInformationInTodoItem(todoItems: TodoItems)
		= UpdateInformationInTodoItemCommand(todoItems)

	class CrossCuttingConcerns<T>(private val validator: (command: T) -> T,
								  private val commandImpl: T) {
		val command: T
			get() = validator.invoke(commandImpl)
	}
}

@Configuration
class PostgresTestDatabase {
	@Bean
	fun dataSource(): DataSource {
		val builder = EmbeddedDatabaseBuilder()
		val db = builder.setType(EmbeddedDatabaseType.H2).build()
		Liquibase("liquibase/master.xml", ClassLoaderResourceAccessor(), JdbcConnection(db.connection)).update("whatever")
		return db
	}

	@Bean
	fun todoItems(dataSource: DataSource, todoItemFactory: TodoItemFactory)
		= SqlDBTodoItems(dataSource, todoItemFactory::create)
}

@Configuration
class DomainWiring {
	@Bean
	fun domainEventDispatcher() = DomainEventDispatcher()

	@Bean
	fun todoItemFactory(eventDispatcher: Events) = TodoItemFactory(eventDispatcher::dispatchEvent)
}
