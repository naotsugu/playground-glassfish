plugins {
    war
}

repositories {
    mavenCentral()
}

dependencies {
    providedCompile("jakarta.platform:jakarta.jakartaee-api:11.0.0-M4")
    implementation("com.h2database:h2:2.3.232")
    testImplementation(libs.junit.jupiter)
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}

task<Exec>("run") {

    dependsOn("war")

    // download glassfish-embedded-all
    // val url = "https://repo1.maven.org/maven2/org/glassfish/main/extras/glassfish-embedded-all/8.0.0-M8/glassfish-embedded-all-8.0.0-M8.jar"
    // val dest = layout.projectDirectory.file("glassfish-embedded-all-8.0.0-M8.jar")
    val url = "https://repo1.maven.org/maven2/org/glassfish/main/extras/glassfish-embedded-all/8.0.0-M9/glassfish-embedded-all-8.0.0-M9.jar"
    val dest = layout.projectDirectory.file("glassfish-embedded-all-8.0.0-M9.jar")
    if (!dest.asFile.exists()) uri(url).toURL().openStream().use {
        `java.nio.file`.Files.copy(it, dest.asFile.toPath())
    }

    // exec glassfish-embedded-all
    commandLine("java",
        "-Duser.language=en", "-Duser.country=US",
        "-jar", dest.asFile.absolutePath, "build/libs/app.war")

    standardInput = System.`in`
}
