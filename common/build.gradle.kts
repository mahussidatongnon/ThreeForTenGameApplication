plugins {
    kotlin("jvm") version "1.9.25"
}

group = "com.miageia2.threefortengame"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    testImplementation(kotlin("test"))
}

dependencies {
}

tasks.test {
    useJUnitPlatform()
}