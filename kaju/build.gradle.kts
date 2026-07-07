
plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.android.library)
    alias(libs.plugins.maven.publish)
    signing
}

group = "systems.untangle"
version = "0.1.1"

kotlin {
    jvm("desktop")
    androidLibrary {
        namespace = "systems.untangle.kaju"
        compileSdk = libs.versions.compileSdk.get().toInt()
        minSdk = libs.versions.minSdk.get().toInt()
    }

    sourceSets {
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.ui)
            implementation(compose.foundation)
            implementation(compose.material)
            implementation(compose.components.uiToolingPreview)
        }
    }
}

mavenPublishing {
    publishToMavenCentral()
    signAllPublications()

    coordinates(
      group.toString(),
      "kaju",
      version.toString()
    )

    pom {
        name = "Kaju"
        description = "Kotlin Multiplatform Compose tree view library"
        inceptionYear = "2026"
        url = "https://github.com/vottini/kaju"

        licenses {
            license {
                name = "MIT"
                url = "https://opensource.org/licenses/MIT"
            }
        }

        developers {
            developer {
                name = "Gustavo Venturini"
                email = "gustavo.c.venturini@gmail.com"
            }
        }

        scm {
            url = "https://github.com/vottini/kaju"
            connection = "scm:git:git://github.com/vottini/kaju.git"
            developerConnection = "scm:git:ssh://git@github.com/vottini/kaju.git"
        }
    }
}
