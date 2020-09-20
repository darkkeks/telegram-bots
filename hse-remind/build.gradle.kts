plugins {
    kotlin("jvm")
    kotlin("plugin.spring")
    id("org.springframework.boot")
}

springBoot {
    mainClassName = "ru.darkkeks.telegram.hseremind.HseRemindAppKt"
}

dependencies {
    implementation(project(":core"))

    implementation("org.springframework.boot:spring-boot-starter-data-mongodb:2.3.2.RELEASE")

    implementation("com.squareup.okhttp3:logging-interceptor:3.14.9")
}
