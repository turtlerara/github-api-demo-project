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
    jacocoTestReport {
        dependsOn(test)
        reports {
            html.required = true
            xml.required = false
            csv.required = false
        }
    }

    test {
        finalizedBy(jacocoTestReport)
        useJUnitPlatform()
    }
}