plugins {
    val kotlinVersion = "1.6.10"
    kotlin("jvm") version kotlinVersion
    kotlin("plugin.serialization") version kotlinVersion

    id("net.mamoe.mirai-console") version "2.10.0"
}

dependencies {
    api("net.mamoe:mirai-silk-converter:0.0.5")

    val ktorVersion = "1.6.8"
    @Suppress("GradlePackageUpdate")
    implementation("io.ktor:ktor-client-core:$ktorVersion")
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.4.20")
}

group = "indi.goldenwater.miraichaosbot"
version = "1.7.0"

repositories {
    maven("https://maven.aliyun.com/repository/public")
    mavenCentral()
}
