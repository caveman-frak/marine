plugins {
    id("marine.java-conventions")
    id("application")
    id("org.jooq.jooq-codegen-gradle")
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.5.0")
    implementation("org.springframework.boot:spring-boot-devtools")
    runtimeOnly("org.liquibase:liquibase-core")
    runtimeOnly("com.h2database:h2")
    implementation("org.jooq:jooq:3.19.8")
    implementation("org.jooq:jooq-meta:3.19.8")
    implementation("org.jooq:jooq-meta-extensions:3.19.8")
//    jooqCodegen("org.jooq:jooq-meta-extensions-liquibase:3.19.8")
//    jooqCodegen("org.liquibase:liquibase-core")
    jooqCodegen("org.jooq:jooq-meta-extensions-hibernate:3.19.8")
    jooqCodegen("org.jooq:jooq-meta-extensions:3.19.8")
//    jooqCodegen("org.hibernate.orm:hibernate-core")
//    jooqCodegen("org.slf4j:slf4j-jdk14")
//    jooqCodegen("org.apache.maven:maven-settings-builder")

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