plugins {
    id("project.java-app-conventions")
    alias(libs.plugins.springBoot) apply false
}

apply(plugin = "io.spring.dependency-management")

group = "com.demo"
description = "Demo App"
version = "1.0"

dependencies {
    // Testing
    testImplementation(libs.spock.spring)
}
