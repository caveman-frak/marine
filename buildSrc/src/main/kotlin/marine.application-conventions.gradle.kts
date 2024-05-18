plugins {
    id("marine.java-conventions")
    id("application")
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.5.0")
    implementation("org.springframework.boot:spring-boot-devtools")
    runtimeOnly("org.liquibase:liquibase-core")
    runtimeOnly("com.h2database:h2")
}

testing {
    suites {
        val test by getting(JvmTestSuite::class)

        register<JvmTestSuite>("integrationTest") {
            useJUnitJupiter()
            dependencies {
                implementation(project())
                implementation(testFixtures(project()))
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

tasks.named("check") {
    dependsOn(testing.suites.named("integrationTest"))
}
tasks.named<ProcessResources>("processResources") {
    filesMatching("application.*") {
        expand(project.properties)
    }
}

springBoot {
    buildInfo()
}