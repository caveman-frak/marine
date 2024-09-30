plugins {
    id("marine.application-conventions")
}

dependencies {
    implementation("org.springframework.shell:spring-shell-starter:3.3.3")
}

testing {
    suites {
        val integrationTest by getting(JvmTestSuite::class) {
            dependencies {
            }
        }
    }
}