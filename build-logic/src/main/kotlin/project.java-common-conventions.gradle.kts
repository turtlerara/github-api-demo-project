plugins {
    java
    groovy
    jacoco
    id("io.freefair.lombok")
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    mavenCentral()
}

tasks {
    test {
        useJUnitPlatform()
    }
}