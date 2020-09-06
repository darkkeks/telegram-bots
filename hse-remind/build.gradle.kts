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
}
