@file:Suppress("UnstableApiUsage")

import de.undercouch.gradle.tasks.download.Download

plugins {
    id("marine.java-conventions")
    id("application")
    id("de.undercouch.download")
    id("com.gorylenko.gradle-git-properties")
}

dependencies {
    implementation(project(":shared"))
    implementation(project(":wire"))
    testFixturesImplementation(testFixtures(project(":shared")))
    testFixturesImplementation(testFixtures(project(":wire")))
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-devtools")
    implementation("io.opentelemetry.instrumentation:opentelemetry-spring-boot-starter")
    runtimeOnly("org.liquibase:liquibase-core")
    runtimeOnly("com.h2database:h2")
    implementation("com.querydsl:querydsl-core:5.1.0")
    implementation("com.querydsl:querydsl-jpa:5.1.0")
    annotationProcessor("com.querydsl:querydsl-apt:5.1.0:jakarta")
    annotationProcessor("jakarta.persistence:jakarta.persistence-api")
}

testing {
    suites {
        val test by getting(JvmTestSuite::class)

        withType<JvmTestSuite> {
            dependencies {
                implementation(project(":shared"))
                implementation(project(":wire"))
                implementation(testFixtures(project(":shared")))
                implementation(testFixtures(project(":wire")))
            }
        }

        register<JvmTestSuite>("integrationTest") {
            useJUnitJupiter()
            dependencies {
                compileOnly("org.springframework:spring-web")
                compileOnly("org.springframework.data:spring-data-jpa")
                compileOnly("jakarta.persistence:jakarta.persistence-api")
                compileOnly("com.fasterxml.jackson.core:jackson-annotations")
                runtimeOnly("org.liquibase:liquibase-core")
                runtimeOnly("com.h2database:h2")
            }

            targets {
                all {
                    testTask.configure {
                        shouldRunAfter(test)
                    }
                }
            }
        }
    }
}

class OtelAgent {
    fun jar(): String {
        return "opentelemetry-javaagent.jar"
    }

    fun dir(): String {
        return rootProject.projectDir.absolutePath
    }

    fun path(): String {
        return dir() + "/.otel-agent/" + jar()
    }

    fun url(): String {
        return "https://github.com/open-telemetry/opentelemetry-java-instrumentation/releases/latest/download/" + jar()
    }
}

val otelAgent = OtelAgent()

tasks {
    val downloadOtelAgent by registering(Download::class) {
        description = "Download OTEL Agent"
        src(otelAgent.url())
        dest(otelAgent.path())
        overwrite(false)
        onlyIfNewer(true)
    }
}

tasks.named("check") {
    dependsOn(testing.suites.named("integrationTest"))
}
tasks.withType<JavaExec> {
// --- disabled open telemetry java agent ---
//    dependsOn("downloadOtelAgent")
//    jvmArgs(setOf("-javaagent:${otelAgent.path()}"))
//    systemProperty("otel.experimental.config.file", "../env/otel-config.yaml")
}

springBoot {
    buildInfo {
        excludes.set(setOf("time"))
    }
}