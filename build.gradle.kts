plugins {
    id("architectury-plugin") version "3.4.+"
    id("dev.architectury.loom") version "0.11.0.+" apply false
    base
}

subprojects {
    apply(plugin = "dev.architectury.loom")

    dependencies {
        val minecraftVersion: String by rootProject
        "minecraft"("com.mojang:minecraft:$minecraftVersion")
        "mappings"("net.fabricmc:yarn:1.18.2+build.+:v2")
    }
}

allprojects {
    apply(plugin = "java")
    apply(plugin = "architectury-plugin")
    apply(plugin = "maven-publish")

    base.archivesName.set("SettxiCloth")
    group = "dev.isxander"
    version = "1.0.0"

    repositories {
        mavenCentral()
        maven("https://maven.fabricmc.net/")
        maven("https://jitpack.io/")
        maven("https://maven.shedaniel.me/")
    }
}