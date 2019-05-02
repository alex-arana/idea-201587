import org.gradle.api.tasks.wrapper.Wrapper.DistributionType
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    idea

    val kotlinVersion = "1.3.31"
    kotlin("jvm") version kotlinVersion
    kotlin("kapt") version kotlinVersion
    id("org.jetbrains.kotlin.plugin.allopen") version kotlinVersion
    id("org.jetbrains.kotlin.plugin.spring") version kotlinVersion

    // Spring Boot
    id("org.springframework.boot") version "2.1.4.RELEASE"
    id("io.spring.dependency-management") version "1.0.7.RELEASE"
}

repositories {
    mavenCentral()
}

tasks {
    bootJar {
        archiveClassifier.set("boot")
    }

    jar {
        enabled = true
    }

    withType(KotlinCompile::class.java) {
        kotlinOptions {
            jvmTarget = JavaVersion.VERSION_1_8.toString()
            freeCompilerArgs = listOf("-Xjsr305=strict")
        }
    }

    withType(Wrapper::class.java) {
        gradleVersion = "5.4.1"
        distributionType = DistributionType.ALL
    }
}

dependencies {
    compile(kotlin("stdlib-jdk8"))
    compile("org.jetbrains.kotlin:kotlin-reflect")
    compile("org.springframework.boot:spring-boot-starter")

    kapt("org.springframework.boot:spring-boot-configuration-processor")
}

springBoot {
    buildInfo {
        properties {
            additional = mapOf(
                "description" to project.description
            )
        }
    }
}

defaultTasks("build")
