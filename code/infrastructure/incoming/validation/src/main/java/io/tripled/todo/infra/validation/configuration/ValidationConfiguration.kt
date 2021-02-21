package io.tripled.todo.infra.validation.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean

@Configuration
class ValidationConfiguration {
	@Bean
	fun validator() = LocalValidatorFactoryBean()
}
