plugins {
    id("project.java-common-conventions")
    `java-library`
}

java {
    withJavadocJar()
    withSourcesJar()
}

tasks {
    javadoc {
        (options as StandardJavadocDocletOptions).addBooleanOption("Xdoclint:-missing", true)
    }
}
