plugins {
    id("marine.java-conventions")
    id("application")
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.4.0")
    implementation("org.mapstruct:mapstruct:1.6.0.Beta1")
    implementation("org.projectlombok:lombok-mapstruct-binding:0.2.0")
    annotationProcessor("org.mapstruct:mapstruct-processor:1.6.0.Beta1")
}

testing {
    suites {
        configureEach {
            if (this is JvmTestSuite) {
                dependencies {
                    implementation(platform(org.springframework.boot.gradle.plugin.SpringBootPlugin.BOM_COORDINATES))
                    implementation("org.springframework.boot:spring-boot-starter-test")
                    compileOnly("org.projectlombok:lombok:1.18.26")
                    annotationProcessor("org.projectlombok:lombok:1.18.26")
                    compileOnly("org.springframework:spring-web")
                    compileOnly("org.springframework.data:spring-data-jpa")
                    compileOnly("jakarta.persistence:jakarta.persistence-api")
                    compileOnly("com.fasterxml.jackson.core:jackson-annotations")
                }
            }
        }

        val integrationTest by registering(JvmTestSuite::class) {
            useJUnitJupiter()
            dependencies {
                implementation(project())
            }
        }
    }
}