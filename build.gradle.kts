plugins {
    id("java")
    id("application")
}

repositories {
    gradlePluginPortal()
    mavenCentral()
}
group = "com.highlylogical"
version = "1.0-SNAPSHOT"


dependencies {
    implementation("io.javalin:javalin:5.6.2")
    implementation("com.datastax.oss:java-driver-core:4.16.0")
    implementation("org.slf4j:slf4j-simple:2.0.9")
    implementation("org.crac:crac:1.5.0")
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

application {
    mainClass = "com.highlylogical.Main"
}

tasks.test {
    useJUnitPlatform()
}