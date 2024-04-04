plugins {
  `java-library`
}

java {
  toolchain {
    languageVersion.set(JavaLanguageVersion.of(21))
  }
}

repositories {
  mavenCentral()
}

dependencies {
  implementation("org.slf4j:slf4j-api:2.0.12")
  implementation("org.slf4j:slf4j-simple:2.0.12")
  implementation("commons-codec:commons-codec:1.16.1")
  implementation("org.apache.commons:commons-lang3:3.14.0")
  implementation("org.apache.commons:commons-collections4:4.4")
  implementation("org.apache.lucene:lucene-core:9.10.0")
  implementation("org.apache.lucene:lucene-analyzers-common:8.11.3")
  implementation ("com.google.code.gson:gson:2.8.8")
  testImplementation("org.mockito:mockito-core:5.11.0")
  testImplementation("com.opencsv:opencsv:5.9")
  testImplementation("org.hamcrest:hamcrest:2.2")
  testImplementation("org.junit.jupiter:junit-jupiter:5.9.1")
}

tasks.named<Test>("test") {
  useJUnitPlatform()
}
