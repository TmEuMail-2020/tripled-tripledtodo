package io.tripled.todo.user

import io.tripled.todo.UserId

class UserService (private val users: Collection<UserId>) {
    fun exists(userId: UserId) = users.contains(userId)
}
