plugins {
    kotlin("jvm") version "2.2.0"
    id("io.gitlab.arturbosch.detekt") version "1.23.8"
    id("org.jlleitschuh.gradle.ktlint") version "13.1.0"
}

group = "com.wire.apps.jira"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

detekt {
    toolVersion = "1.23.7"
    config.setFrom(file("$rootDir/config/detekt/detekt.yml"))
    baseline = file("$rootDir/config/detekt/baseline.xml")
    parallel = true
    buildUponDefaultConfig = true
    source.setFrom("src/main/kotlin")
}

dependencies {
    implementation("io.javalin:javalin:6.7.0")
    implementation("org.slf4j:slf4j-simple:2.0.16")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.17.2")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.20.1")
    implementation("io.insert-koin:koin-core:4.1.1")
    implementation("com.wire:wire-apps-jvm-sdk:0.0.18")
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}
