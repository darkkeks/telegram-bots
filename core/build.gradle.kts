plugins {
    kotlin("jvm")
    kotlin("plugin.spring")
}

dependencies {
    api(kotlin("stdlib-jdk8"))
    api(kotlin("reflect"))

    api("com.google.guava:guava:29.0-jre")

    api("org.springframework.boot:spring-boot-starter:2.3.2.RELEASE")
    api("com.fasterxml.jackson.module:jackson-module-kotlin:2.11.1")
    api("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:2.11.1")

    api("com.squareup.retrofit2:retrofit:2.9.0")
    api("com.squareup.retrofit2:converter-jackson:2.9.0")

    api("io.github.config4k:config4k:0.4.2")

    testImplementation("junit", "junit", "4.12")
}
