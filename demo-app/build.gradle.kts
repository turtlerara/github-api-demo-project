plugins {
    id("project.java-app-conventions")
    alias(libs.plugins.springBoot)
}

apply(plugin = "io.spring.dependency-management")

group = "com.demo"
description = "Demo App"
version = "1.0"

springBoot {
    buildInfo()
}

dependencies {
    // Spring Boot
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    developmentOnly("org.springframework.boot:spring-boot-devtools")

    // Springdoc
    implementation(libs.springdoc)

    // Testing
    testImplementation(libs.spock.spring)
}
