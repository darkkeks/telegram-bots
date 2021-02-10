import org.jooq.meta.jaxb.Logging

plugins {
    kotlin("jvm")
    kotlin("plugin.spring")
    id("nu.studer.jooq") version "5.2"
    id("org.springframework.boot")
}

springBoot {
    mainClassName = "ru.darkkeks.kksstat.KksStatAppKt"
}

dependencies {
    implementation(project(":core"))

    implementation("com.squareup.okhttp3:logging-interceptor:3.14.9")
    implementation("org.springframework.boot:spring-boot-starter-web")

    implementation("org.springframework.boot:spring-boot-starter-jooq")
    implementation("org.postgresql:postgresql:42.2.15")
    implementation("org.jooq:jooq-meta:3.14.7")

    jooqGenerator("org.postgresql:postgresql:42.2.14")
}

java {
    sourceSets.create("generated") {
        java.srcDir("src/generated/java")
    }
}

jooq {
    version.set(dependencyManagement.importedProperties["jooq.version"])
    configurations {
        create("main") {
            // omit task dependency from compileJava to generateJooq
            generateSchemaSourceOnCompilation.set(false)

            jooqConfiguration.apply {
                logging = Logging.WARN
                jdbc.apply {
                    driver = "org.postgresql.Driver"
                    url = "jdbc:postgresql://localhost:5432/kks"
                    user = "postgres"
                    password = "root"
                }
                generator.apply {
                    name = "org.jooq.codegen.DefaultGenerator"
                    database.apply {
                        name = "org.jooq.meta.postgres.PostgresDatabase"
                        inputSchema = "public"
                    }
                    target.apply {
                        packageName = "ru.darkkeks.kksstat.schema"
                        directory = "src/generated/java"
                    }
                }
            }
        }
    }
}
