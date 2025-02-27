plugins {
    `kotlin-dsl`
}

val libs = the<org.gradle.accessors.dm.LibrariesForLibs>()

dependencies {
    implementation(libs.lombok.plugin)
}