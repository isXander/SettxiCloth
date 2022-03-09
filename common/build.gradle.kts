plugins {
    kotlin("jvm") version "1.6.10"
    kotlin("plugin.serialization") version "1.6.10"
}

architectury {
    common()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.2")
    api("dev.isxander:settxi:2.1.0")
    modApi("me.shedaniel.cloth:cloth-config:6.+")
}

publishing {
    publications {
        register<MavenPublication>("settxi") {
            groupId = "dev.isxander"
            artifactId = "settxi-cloth-impl"

            from(components["java"])
        }
    }

    repositories {
        if (hasProperty("woverflow.token")) {
            println("Publishing ${project.name} to W-OVERFLOW")
            maven(url = "https://repo.woverflow.cc/releases") {
                credentials {
                    username = "xander"
                    password = property("woverflow.token") as? String
                }
            }
        }
    }
}
