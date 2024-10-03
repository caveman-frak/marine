@file:Suppress("UnstableApiUsage")

import de.undercouch.gradle.tasks.download.Download

plugins {
    application
    id("marine.java-conventions")
    id("de.undercouch.download")
    id("com.gorylenko.gradle-git-properties")
}

val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

dependencies {
    implementation(project(":shared"))
    implementation(project(":wire"))
    testFixturesImplementation(testFixtures(project(":shared")))
    testFixturesImplementation(testFixtures(project(":wire")))
    implementation(libs.findBundle("spring-boot-web").get())
    implementation(libs.findBundle("spring-data").get())
    implementation(libs.findLibrary("telemetry").get())
    runtimeOnly(libs.findBundle("data").get())
    implementation(libs.findBundle("querydsl").get())
    annotationProcessor(variantOf(libs.findLibrary("querydsl-apt").get()) { classifier("jakarta") })
    annotationProcessor(libs.findLibrary("jakarta-persistence").get())
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
                compileOnly.add(libs.findLibrary("spring-web").get())
                compileOnly.add(libs.findLibrary("spring-data-jpa").get())
                compileOnly.add(libs.findLibrary("jakarta-persistence").get())
                compileOnly.add(libs.findLibrary("jackson-annotations").get())
                runtimeOnly.bundle(libs.findBundle("data").get())
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