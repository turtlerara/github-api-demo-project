import org.springframework.boot.gradle.plugin.SpringBootPlugin

plugins {
    base
    `jacoco-report-aggregation`
    alias(libs.plugins.springBoot) apply false
}

dependencies {
    jacocoAggregation(platform(SpringBootPlugin.BOM_COORDINATES))
    jacocoAggregation(project(":github-api-client-spring6"))
}

reporting {
    reports {
        @Suppress("UnstableApiUsage")
        val aggregateJacocoTestReport by creating(JacocoCoverageReport::class) {
            testSuiteName = "test"
            reportTask {
                reports {
                    html.required = true
                    xml.required = false
                    csv.required = false
                }
            }
        }
    }
}

tasks {
    wrapper {
        gradleVersion = libs.versions.gradlew.get()
    }

    check {
        dependsOn(named<JacocoReport>("aggregateJacocoTestReport"))
    }
}