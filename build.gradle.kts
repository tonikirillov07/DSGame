plugins {
    id("java")
}

group = "com.ds"
version = "1.0-SNAPSHOT"

var lwjglJarsPath = "libs/lwjgl-2.8.0/jar/"
var slickJarsPath = "libs/slick/"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.slf4j:slf4j-api:1.7.30")
    implementation("ch.qos.logback:logback-classic:1.4.14")
    implementation("org.jetbrains:annotations:24.0.0")
    implementation(files("libs/jpct/jpct.jar"))
    implementation(files(lwjglJarsPath + "AppleJavaExtensions.jar", lwjglJarsPath + "asm-debug-all.jar", lwjglJarsPath + "jinput.jar", lwjglJarsPath + "lwjgl.jar",
        lwjglJarsPath + "lwjgl-debug.jar", lwjglJarsPath + "lwjgl_test.jar", lwjglJarsPath + "lwjgl_util.jar", lwjglJarsPath + "lwjgl_util_applet.jar", lwjglJarsPath + "lzma.jar"))
    implementation(files(slickJarsPath + "slick2d-core-1.0.2.jar"))
    implementation(files(slickJarsPath + "slick-util.jar"))
    implementation(files(slickJarsPath + "jorbis-0.0.15.jar"))
    implementation(files(slickJarsPath + "jogg-0.0.7.jar"))
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}