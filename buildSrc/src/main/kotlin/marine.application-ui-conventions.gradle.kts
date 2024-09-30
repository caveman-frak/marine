plugins {
    id("marine.application-conventions")
    id("marine.tailwind-build")
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
    implementation("io.github.wimdeblauwe:htmx-spring-boot-thymeleaf:3.5.1")
    runtimeOnly("org.webjars:webjars-locator-core")
    runtimeOnly("org.webjars.npm:htmx.org:2.0.2")
}

testing {
    suites {
        val integrationTest by getting(JvmTestSuite::class) {
            dependencies {
                implementation("org.seleniumhq.selenium:htmlunit3-driver:4.23.0")
            }
        }
    }
}