import org.jooq.meta.jaxb.Logging

plugins {
    kotlin("jvm")
    kotlin("plugin.spring")
    id("nu.studer.jooq") version "5.2"
    id("org.springframework.boot")
}

springBoot {
    mainClass.set("ru.darkkeks.kksstat.KksStatAppKt")
}

dependencies {
    implementation(project(":core"))

    implementation("org.springframework.boot:spring-boot-starter-web")

    implementation("org.springframework.boot:spring-boot-starter-jooq")
    implementation("org.postgresql:postgresql")
    implementation("org.jooq:jooq-meta")

    jooqGenerator("org.postgresql:postgresql")
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
