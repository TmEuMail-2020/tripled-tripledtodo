package io.tripled.todo.testing

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.core.test.AssertionMode
import io.tripled.todo.domain.TodoItems
import io.tripled.todo.fakes.FakeTodoItems
import io.tripled.todo.fakes.TestTodoItems

abstract class TodoItemTest<TESTSUBJECT>(testSubjectCreator: (TodoItems, dispatchEvent: (Any) -> Unit) -> TESTSUBJECT,
                                         testCases: BehaviorSpec.(TESTSUBJECT, TestTodoItems) -> Unit = { _: TESTSUBJECT, _: TestTodoItems -> }) : BehaviorSpec() {
    init {
        assertions = AssertionMode.Error
        val fakeTodoItems = FakeTodoItems()
        val testTodoItems: TestTodoItems = fakeTodoItems
        testCases(testSubjectCreator.invoke(fakeTodoItems, fakeTodoItems::dispatchEvent), testTodoItems)
    }
}