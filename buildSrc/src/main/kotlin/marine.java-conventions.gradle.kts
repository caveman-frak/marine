import gradle.kotlin.dsl.accessors._550befb596988a36a92ff8eedca87611.implementation

plugins {
    id("java")
    id("jvm-test-suite")
}

group = "co.uk.bluegecko.marine"
version = "1.0"

dependencies {
    constraints {
        implementation("org.springframework.boot:spring-boot-starter-json:3.0.4")
        implementation("org.springframework.boot:spring-boot-starter-validation:3.0.4")
        implementation("org.springframework.boot:spring-boot-starter-web:3.0.4")
        implementation("org.springframework.boot:spring-boot-starter-data-jpa:3.0.4")
        implementation("org.springframework.boot:spring-boot-starter-actuator:3.0.4")
        testImplementation("org.springframework.boot:spring-boot-starter-test:3.0.4")
    }
}
dependencies {
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}
