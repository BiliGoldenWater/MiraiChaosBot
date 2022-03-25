plugins {
    val kotlinVersion = "1.6.10"
    kotlin("jvm") version kotlinVersion
    kotlin("plugin.serialization") version kotlinVersion

    id("net.mamoe.mirai-console") version "2.10.0"
}

dependencies {
    val ktorVersion = "1.6.7"
    implementation("io.ktor:ktor-client-core:$ktorVersion")
}

group = "indi.goldenwater.miraichaosbot"
version = "1.5.3"

repositories {
    maven("https://maven.aliyun.com/repository/public")
    mavenCentral()
}
