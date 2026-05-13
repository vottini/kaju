plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.compose.compiler)
}

dependencies {
    implementation(project(":kaju"))
    implementation(compose.desktop.currentOs)
    implementation(compose.material)
}

compose.desktop {
    application {
        mainClass = "systems.untangle.kaju.preview.MainKt"
    }
}
