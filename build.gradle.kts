plugins {
    kotlin("jvm")
    `java-library`
}

group = "com.github.piotrros"
version = "1.0.0"

// Repositories są teraz zdefiniowane w settings.gradle.kts projektu nadrzędnego
// repositories {
//     mavenCentral()
// }

dependencies {
    // Kotlin standard library
    implementation(kotlin("stdlib"))
    
    // Testing
    testImplementation(kotlin("test"))
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.1")
    testImplementation("org.assertj:assertj-core:3.24.2")
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(17)
}


