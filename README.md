# IntelliJ Issue IDEA-201587

## Overview
We have a Gradle project that uses the Spring Boot plugin 'org.springframework.boot' version 2.1.0.RELEASE. Within one of our components we inject the BuildProperties bean which is created as part of the build configuration:

``` build.gradle.kts
springBoot {
    buildInfo {
        properties {
            additional = mapOf(
                "description" to project.description
            )
        }
    }
}
```

Everything works well when we run the app from Gradle (bootRun) but if we attempt to launch the app from IntelliJ IDEA it fails with the following error message:
Bean method 'buildProperties' in 'ProjectInfoAutoConfiguration' not loaded because @ConditionalOnResource did not find resource '${spring.info.build.location:classpath:META-INF/build-info.properties}'

The said resource is created by Gradle during the build phase but not by IntelliJ IDEA to its output folders which prevents us from being able to run the application or debug any tests within the IDE.

If we change the IDE configuration to delegate the build/run actions to Gradle then we forego the ability to make changes in the IDE that are reflected in Gradle's output directories which is already the subject of another issue: [IDEA-166943](https://youtrack.jetbrains.com/issue/IDEA-166943)

I have also tried to run the bootBuildInfo task before launch within the Run/Debug configuration but it still get the same error.

## Building the application
In order to build the program, the following is required

- Java 8 JDK
- A working Internet connection (to download all library dependencies required to build the project). The build script requires access to the Maven Central repository.

The output of the Gradle build for this module is a Spring Boot executable JAR which can be run in standalone mode using the following command:

```bash
$ ./gradlew clean build
```

If everything goes well you will have created the standalone Spring Boot executable JAR in the following directory location relative to the top-level project folder:

```
build/libs/
├── idea-201587-1.0.0-SNAPSHOT.jar
```

## Running the application using Gradle
In order to build and run this project locally, run the following command:
~~~~
$ ./gradlew bootRun
~~~~
NOTE: This assumes that you are running this command from the top-level directory in the project top-level folder and that a Java 8 runtime is installed locally.

## Testing in IDEA (Reproducing the error)
Launch IntelliJ IDEA and import the project. Launch the main application class: `Application.kt`

You should see the following error displayed on the *Console* window:

```
***************************
APPLICATION FAILED TO START
***************************

Description:

Parameter 0 of method commandLineRunner in com.foo.fighting.Application required a bean of type 'org.springframework.boot.info.BuildProperties' that could not be found.

The following candidates were found but could not be injected:
	- Bean method 'buildProperties' in 'ProjectInfoAutoConfiguration' not loaded because @ConditionalOnResource did not find resource '${spring.info.build.location:classpath:META-INF/build-info.properties}'

```
