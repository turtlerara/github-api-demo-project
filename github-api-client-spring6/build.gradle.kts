import io.spring.gradle.dependencymanagement.dsl.DependencyManagementExtension
import org.springframework.boot.gradle.plugin.SpringBootPlugin

plugins {
    id("project.java-lib-conventions")
    alias(libs.plugins.springBoot) apply false
}

apply(plugin = "io.spring.dependency-management")

group = "com.demo.client"
description = "GitHub API Client (Spring 6)"
version = "0.1.0"

the<DependencyManagementExtension>().apply {
    imports {
        mavenBom(SpringBootPlugin.BOM_COORDINATES)
    }
}

dependencies {
    api(libs.jspecify)
    api("org.slf4j:slf4j-api")
    api("org.apache.commons:commons-lang3")
    api("com.fasterxml.jackson.core:jackson-databind")
    api("org.springframework:spring-web")

    // Testing
    testImplementation(libs.spock.core)
    testImplementation("net.bytebuddy:byte-buddy")
    testImplementation(libs.objenesis)
    testImplementation("org.slf4j:slf4j-simple")
}
