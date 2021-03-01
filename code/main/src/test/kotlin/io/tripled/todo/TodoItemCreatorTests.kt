package io.tripled.todo

import io.tripled.todo.domain.TodoItem
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class TodoItemCreatorTests {

	@Test
	fun dispatchingAnEvent() {
		// given
		val eventDispatcher = { _: Any -> {}}
		val snapshot = TodoItem.Snapshot(
			TodoId.existing("abc-123"),
			"Painting",
			"Paint living room white",
			TodoItemStatus.CREATED,
		)

		// when
		val todoItem = TodoItemCreator(eventDispatcher::invoke).create(snapshot)

		// then
		assertThat(todoItem).isNotNull
	}

}
