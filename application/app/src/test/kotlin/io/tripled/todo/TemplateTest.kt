package io.tripled.todo

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.core.test.AssertionMode
import io.kotest.matchers.shouldBe

class TemplateTest : BehaviorSpec() {
    init {
        assertions = AssertionMode.Error

        given("System in a given state") {
            val initialState = "abc"
            `when`("executing action on the system") {
                val result = initialState.subSequence(0, 1)

                then("We check the result") {
                    result shouldBe "a"
                }
            }
        }
    }
}
