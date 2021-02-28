package io.tripled.todo

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class EventListTests {

	data class TestEvent(val value: String)

	@Test
	fun dispatchingAnEvent() {
		// given
		val anEvent = TestEvent("abc")
		val domainEventDispatcher = DomainEventDispatcher()

		// when
		domainEventDispatcher.dispatchEvent(anEvent)

		// then
		assertThat(domainEventDispatcher.events).isEqualTo(listOf(TestEvent("abc")))
	}

}
