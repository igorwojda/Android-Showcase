// Top-level build file where you can add configuration options common to all sub-projects/modules.

// plugins and dependencies are defined in the settings.gradle file
// utilising Gradle dependency management

plugins {
    id("com.android.application") apply false
    id("com.android.library") apply false
    kotlin("android") apply false
    id("io.gitlab.arturbosch.detekt")
}

subprojects {
    apply {
        plugin("io.gitlab.arturbosch.detekt")
    }

    detekt {
        config = rootProject.files("detekt.yml")
    }
}
