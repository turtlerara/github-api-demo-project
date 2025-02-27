plugins {
    base
    `jacoco-report-aggregation`
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