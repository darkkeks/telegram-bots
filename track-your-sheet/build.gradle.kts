plugins {
    kotlin("jvm")
    kotlin("plugin.spring")
    id("org.springframework.boot")
}

springBoot {
    mainClassName = "ru.darkkeks.telegram.trackyoursheet.ApplicationKt"
}

dependencies {
    implementation(project(":core"))

    implementation("org.springframework.boot:spring-boot-starter-data-jdbc:2.3.2.RELEASE")
    implementation("org.postgresql:postgresql:42.2.15")

    implementation("com.google.api-client:google-api-client:1.30.4")
    implementation("com.google.oauth-client:google-oauth-client-jetty:1.30.4")
    implementation("com.google.apis:google-api-services-sheets:v4-rev581-1.25.0")
}
