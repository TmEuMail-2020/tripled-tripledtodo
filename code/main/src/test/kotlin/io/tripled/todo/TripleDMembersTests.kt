package io.tripled.todo

import io.tripled.todo.user.UserService
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

class TripleDMembersTests {

	@ParameterizedTest
	@MethodSource("provideUsers")
	fun testTripleDMembersAsAssignees(userId: UserId, exists: Boolean) {
		// given

		// when
		val result = UserService(listOf(UserId.existing("kris"))).exists(userId)

		// then
		assertThat(result).isEqualTo(exists)
	}


	companion object {
		@JvmStatic
		private fun provideUsers(): Stream<Arguments>? {
			return Stream.of(
				Arguments.of(UserId.existing("kris"), true),
				Arguments.of(UserId.existing("robert"), false),
			)
		}
	}
}
