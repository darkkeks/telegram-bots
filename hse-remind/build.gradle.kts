plugins {
    kotlin("jvm")
    kotlin("plugin.spring")
    id("org.springframework.boot")
}

springBoot {
    mainClass.set("ru.darkkeks.telegram.hseremind.HseRemindAppKt")
}

tasks.getByName<Jar>("jar") {
    enabled = false
}

dependencies {
    implementation(project(":core"))

    implementation("org.springframework.boot:spring-boot-starter-data-mongodb")

    implementation("com.google.api-client:google-api-client:1.23.0")
    implementation("com.google.oauth-client:google-oauth-client-jetty:1.23.0")
    implementation("com.google.apis:google-api-services-youtube:v3-rev222-1.25.0")
}
