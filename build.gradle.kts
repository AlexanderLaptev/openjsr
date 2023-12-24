plugins {
    id("java")
    id("idea")
    id("application")
    id("org.openjfx.javafxplugin") version "0.1.0"
}

group = "org.openjsr"
version = "0.1.0-alpha"

idea {
    module {
        isDownloadSources = true
    }
}

repositories {
    mavenCentral()
    flatDir {
        dir("lib")
    }
}

javafx {
    version = "21.0.1"
    modules = arrayListOf("javafx.controls", "javafx.fxml")
}

application {
    mainModule = "org.openjsr.main"
    mainClass = "org.openjsr.app.JsrApplication"
}

dependencies {
    implementation(":MathLib-2.0.0-module")
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.jar {
    archiveBaseName = "openjsr"
}

tasks.test {
    useJUnitPlatform()
}
