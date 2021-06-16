plugins {
    id("java")   
}

repositories {
    mavenCentral()
}

dependencies {
    constraints {
        implementation("io.vertx:vertx-web:4.1.0")
        implementation("io.vertx:vertx-core:4.1.0")
        implementation("ch.qos.logback:logback-classic:1.2.3")
        compileOnly("io.vertx:vertx-codegen:4.1.0")
        implementation("io.vertx:vertx-service-proxy:4.1.0")
        annotationProcessor("io.vertx:vertx-codegen:4.1.0:processor")
        annotationProcessor("io.vertx:vertx-service-proxy:4.1.0")
        testImplementation("org.junit.jupiter:junit-jupiter-api:5.7.1")
        testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}
}

tasks.test {
    // Use junit platform for unit tests.
    useJUnitPlatform()
}

