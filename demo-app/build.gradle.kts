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
    // Apache Commons
    implementation("org.apache.commons:commons-lang3")
    implementation(libs.commons.text)

    // GitHub API Client
    implementation(project(":github-api-client-spring6"))

    // Spring Boot
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-cache")
    developmentOnly("org.springframework.boot:spring-boot-devtools")

    // Springdoc
    implementation(libs.springdoc)

    // MapStruct
    implementation(libs.mapstruct)
    annotationProcessor(libs.mapstruct.processor)

    // Testing
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation(libs.spock.spring)
}
