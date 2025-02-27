plugins {
    id("project.java-lib-conventions")
    alias(libs.plugins.springBoot) apply false
}

apply(plugin = "io.spring.dependency-management")

group = "com.demo.client"
description = "GitHub API Client (Spring 6)"
version = "0.1.0"

dependencies {
    // Testing
    testImplementation(libs.spock.core)
}
