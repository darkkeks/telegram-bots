import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
    kotlin("plugin.spring")
    id("org.springframework.boot")
}

springBoot {
    mainClassName = "ru.darkkeks.telegram.lifestats.ApplicationKt"
}

dependencies {
    implementation(project(":core"))

    implementation("com.squareup.okhttp3:logging-interceptor:3.14.9")

    implementation("org.springframework.boot:spring-boot-starter-data-jdbc:2.3.2.RELEASE")
    implementation("org.postgresql:postgresql:42.2.15")
}
