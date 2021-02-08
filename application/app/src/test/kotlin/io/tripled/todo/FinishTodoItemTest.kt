package io.tripled.todo

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.core.test.AssertionMode
import io.kotest.matchers.shouldBe
import io.tripled.todo.domain.TodoItem
import io.tripled.todo.fakes.FakeTodoItemsRepository
import io.tripled.todo.fakes.TestTodoItems


class FinishTodoItemTest : BehaviorSpec() {
    init {
        assertions = AssertionMode.Error

        val fakeTodoItemRepository = FakeTodoItemsRepository()
        val testTodoItems: TestTodoItems = fakeTodoItemRepository
        val finishTodoItem: FinishTodoItem = FinishTodoItemCommand(fakeTodoItemRepository)

        given("A todo item that's in progress") {
            val todoId = TodoId.create("abc-123")
            val existingTodoItem = TodoItem.Snapshot(todoId, "title", "description", TodoItem.Status.CREATED)
            fakeTodoItemRepository.assumeExisting = existingTodoItem
            val request = FinishTodoItem.Request(todoId)

            `when`("Finishing the todo") {
                finishTodoItem.create(request)

                then("We verify todo item has been finished") {
                    testTodoItems.lastSaved shouldBe TodoItem.Snapshot(todoId,
                                                                        "title", "description",
                                                                        TodoItem.Status.FINISHED)
                }
            }
        }
    }
}
