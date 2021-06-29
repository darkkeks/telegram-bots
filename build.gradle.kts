import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.5.20" apply false
    kotlin("plugin.spring") version "1.5.20" apply false
    id("org.springframework.boot") version "2.5.0" apply false
}

allprojects {
    group = "ru.darkkeks.telegram.bots"
    version = "0.0.1-SNAPSHOT"

    tasks.withType<KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs = listOf("-Xjsr305=strict")
            freeCompilerArgs = listOf("-Xopt-in=kotlin.ExperimentalStdlibApi")
            jvmTarget = "1.8"
            languageVersion = "1.5"
        }
    }

    repositories {
        mavenCentral()
    }
}

subprojects {
    apply(plugin = "io.spring.dependency-management")
}
