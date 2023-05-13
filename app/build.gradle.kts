/*
 * This file was generated by the Gradle 'init' task.
 */

plugins {
    id("kafka_demo.java-application-conventions")
}

dependencies {
    implementation("org.apache.kafka:kafka-clients:3.4.0")
    implementation("org.apache.logging.log4j:log4j-api:2.20.0")
    runtimeOnly("org.apache.logging.log4j:log4j-core:2.20.0")
}

application {
    // Define the main class for the application.
    mainClass.set("kafka_demo.app.KafkaDemo")
}

tasks.withType<Jar> {
    archiveFileName.set("kafka_demo.jar")
    manifest {
        attributes["Main-Class"] = "kafka_demo.app.KafkaDemo"
    }
}

tasks.create("build-fat", Jar::class) {
    group = "build"
    description = "Creates a self-contained fat JAR of the application that can be run."
    manifest.attributes["Main-Class"] = "kafka_demo.app.KafkaDemo"
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    val dependencies = configurations
        .runtimeClasspath
        .get()
        .map(::zipTree)
    from(dependencies)
    with(tasks.jar.get())
}
