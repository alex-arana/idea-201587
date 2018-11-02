import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.gradle.api.tasks.wrapper.Wrapper.DistributionType
import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    idea

    // TODO externalise this somehow..
    val kotlinVersion = "1.3.0"
    kotlin("jvm") version kotlinVersion
    id("org.jetbrains.kotlin.plugin.allopen") version kotlinVersion
    id("org.jetbrains.kotlin.plugin.spring") version kotlinVersion

    // Spring Boot
    id("org.springframework.boot") version "2.1.0.RELEASE"
    id("io.spring.dependency-management") version "1.0.6.RELEASE"
}

repositories {
    mavenCentral()
}

tasks {
    val bootJar by existing(BootJar::class) {
        classifier = "boot"
    }

    val jar by getting(Jar::class) {
        enabled = true
    }

    withType(KotlinCompile::class.java) {
        kotlinOptions {
            jvmTarget = JavaVersion.VERSION_1_8.toString()
            freeCompilerArgs = listOf("-Xjsr305=strict")
        }
    }

    withType(Wrapper::class.java) {
        gradleVersion = "4.10.2"
        distributionType = DistributionType.ALL
    }
}

dependencies {
    compile(kotlin("stdlib-jdk8"))
    compile("org.jetbrains.kotlin:kotlin-reflect")
    compile("org.springframework.boot:spring-boot-starter")

    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
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
