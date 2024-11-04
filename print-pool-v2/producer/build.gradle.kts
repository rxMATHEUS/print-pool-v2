plugins {
    id("java")
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation("org.apache.activemq:activemq-all:6.1.3")
    implementation("org.apache.logging.log4j:log4j-core:2.24.1")

}