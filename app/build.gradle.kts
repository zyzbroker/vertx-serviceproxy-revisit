plugins {
    id("com.resentek.java-application-conventions")
    id("com.github.johnrengelman.shadow") version("7.0.0")
}

dependencies {
    implementation("io.vertx:vertx-web")
    implementation("io.vertx:vertx-core");
    implementation("io.vertx:vertx-service-proxy")
    implementation("ch.qos.logback:logback-classic")
    compileOnly("io.vertx:vertx-codegen") 
    annotationProcessor("io.vertx:vertx-codegen:.*:processor")
    annotationProcessor("io.vertx:vertx-service-proxy")   
}

application {
  mainClass.set("io.vertx.core.Launcher")
}

  sourceSets {
      create("generated") {
        java.srcDir("${projectDir}/src/generated/java")
        compileClasspath = sourceSets["main"].compileClasspath
        runtimeClasspath = sourceSets["main"].runtimeClasspath 
      }
  }

tasks.jar {
    manifest() {
      attributes(
        mapOf("Main-Verticle" to 
          "com.resentek.app.BootstrapVerticle"))
    }
}

tasks.shadowJar {
   minimize(){
     exclude(dependency("io.vertx:vertx-core"))
   }
}

val generateVertxProxyCodes = task<JavaCompile>("generateVerxProxyCodes") {
  source = sourceSets["main"].java
  classpath = sourceSets["main"].compileClasspath
  options.annotationProcessorPath = sourceSets["main"].compileClasspath
  options.compilerArgs.addAll(listOf(
            "-proc:only",
            "-processor", "io.vertx.codegen.CodeGenProcessor",
            "-Acodegen.output=${projectDir}/src/generated"
    ))
  destinationDir = file("${projectDir}/src/generated")
}

tasks.compileJava {
  dependsOn(generateVertxProxyCodes)
  source += sourceSets["generated"].java
  options.compilerArgs.plus("-proc:none")
}

tasks.clean {
  delete("${projectDir}/src/generated")
}