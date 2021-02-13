package io.tripled.todo

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.core.test.AssertionMode
import io.kotest.matchers.shouldBe
import io.tripled.todo.domain.TodoItem
import io.tripled.todo.fakes.FakeTodoItems
import io.tripled.todo.fakes.TestTodoItems


class CancelTodoItemTest : BehaviorSpec() {
    init {
        assertions = AssertionMode.Error

        val fakeTodoItemRepository = FakeTodoItems()
        val testTodoItems: TestTodoItems = fakeTodoItemRepository
        val cancelTodoItem: CancelTodoItem = CancelTodoItemCommand(fakeTodoItemRepository)

        given("A todo item that's in progress") {
            val todoId = TodoId.create("abc-123")
            val existingTodoItem = TodoItem.Snapshot(todoId, "title", "description", TodoItem.Status.CREATED)
            fakeTodoItemRepository.assumeExisting = existingTodoItem
            val request = CancelTodoItem.Request(todoId)

            `when`("Cancelling the todo") {
                cancelTodoItem.cancel(request)

                then("We verify that the todo item has been cancelled") {
                    testTodoItems.lastSaved shouldBe TodoItem.Snapshot(todoId,
                                                                        "title", "description",
                                                                        TodoItem.Status.CANCELLED)
                }
            }
        }
    }
}
