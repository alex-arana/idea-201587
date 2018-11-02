package com.foo.fighting

import org.springframework.boot.CommandLineRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.info.BuildProperties
import org.springframework.context.annotation.Bean

/**
 * Application main entry point.
 */
@SpringBootApplication
class Application {

    @Bean
    fun commandLineRunner(buildProperties: BuildProperties) = CommandLineRunner {
        println("SpringBoot BuildProperties:")
        buildProperties.forEach { entry ->
            println("  ${entry.key} = ${entry.value}")
        }
    }
}

fun main(args: Array<String>) {
    SpringApplication.run(Application::class.java, *args)
}
