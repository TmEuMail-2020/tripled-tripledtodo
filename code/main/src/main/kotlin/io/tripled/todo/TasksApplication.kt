package io.tripled.todo

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class TasksApplication

fun main(vararg args: String) {
	runApplication<TasksApplication>(*args)
}
