# Settxi Cloth Implementation

A simple library that implements a Cloth Config GUI on top of my settings library, [Settxi](https://github.com/isXander/Settxi)!

**THIS IS A LIBRARY FOR KOTLIN!
JAVA IS NOT SUPPORTED AND THE FOLLOWING
GUIDES ASSUME YOU ALREADY HAVE KOTLIN INSTALLED!**

## Installation

*Groovy DSL*
```groovy
repositories {
    maven { url = 'https://repo.woverflow.cc/' }
}

dependencies {
    // currently there is no user jar to download, so we
    // need to nest the jar. this is okay as both of these
    // are pretty light-weight libraries

    // pick the latest versions, this might not be up to date!
    include implementation("dev.isxander:settxi:2.1.1")
    include modImplementation("dev.isxander:settxi-cloth-impl:1.0.3:fabric-1.19")
}
```

*Kotlin DSL*
```kotlin
repositories {
    maven(url = "https://repo.woverflow.cc/")
}

dependencies {
    // currently there is no user jar to download, so we
    // need to nest the jar. this is okay as both of these
    // are pretty light-weight libraries

    // pick the latest versions, this might not be up to date!
    include(implementation("dev.isxander:settxi:2.1.1"))
    include(modImplementation("dev.isxander:settxi-cloth-impl:1.0.3:fabric-1.19"))
}
```

## Usage

**Creating a Config**
```kotlin
object MyModSettings : SettxiGuiWrapper(LiteralText("My Mod Settings"), File(MinecraftClient.getInstance().runDirectory, "config/mymodconfig.json")) {
    // you can have any implementation of MutableList
    override val settings = mutableListOf<Setting<*>>()

    // Settxi DSL code goes here!
    // you can find a pretty neat example from my mod, Zoomify
    // https://github.com/isXander/Zoomify/blob/1.18/src/main/kotlin/dev/isxander/zoomify/config/ZoomifySettings.kt

    var myIntProperty by int(default = 5) {
        name = "you can have the translation key or literal text here"
        description = "same here"
        category = "and here"
        range = 1..10
    }

    // make sure to load the config file!
    init {
        load()
    }
}
```

**Getting a GUI instance**
```kotlin
// just a one liner!
MyModSettings.clothGui(parentScreenOrNull)
```
