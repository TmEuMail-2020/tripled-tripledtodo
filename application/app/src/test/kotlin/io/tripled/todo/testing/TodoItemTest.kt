package io.tripled.todo.testing

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.core.test.AssertionMode
import io.tripled.todo.fakes.FakeTodoItems
import io.tripled.todo.fakes.TestTodoItems

abstract class TodoItemTest(body: BehaviorSpec.(FakeTodoItems, TestTodoItems) -> Unit = { fakeTodoItems: FakeTodoItems, testTodoItems: TestTodoItems -> }) : BehaviorSpec() {
    init {
        assertions = AssertionMode.Error
        val fakeTodoItems = FakeTodoItems()
        val testTodoItems: TestTodoItems = fakeTodoItems
        body(fakeTodoItems, testTodoItems)
    }
}