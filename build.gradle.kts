// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    id("com.google.gms.google-services") version "4.4.2" apply false
}

buildscript {
    configurations.all {
        resolutionStrategy {
            force("com.google.code.gson:gson:2.11.0")
            force("org.jdom:jdom2:2.0.6.1")
            force("commons-io:commons-io:2.16.1")
            force("com.google.protobuf:protobuf-javalite:3.25.5")
            force("com.google.protobuf:protobuf-java:3.25.5")
            force("io.netty:netty-handler:4.1.108.Final")
            force("io.netty:netty-codec-http2:4.1.108.Final")
            force("io.netty:netty-codec-http:4.1.108.Final")
            force("io.netty:netty-codec:4.1.108.Final")
            force("io.netty:netty-common:4.1.108.Final")
            force("org.bitbucket.b_c:jose4j:0.9.6")
            force("org.apache.commons:commons-compress:1.26.1")
            force("org.bouncycastle:bcprov-jdk18on:1.78.1")
            force("org.bouncycastle:bcpkix-jdk18on:1.78.1")
            force("com.squareup.okio:okio:3.9.0")
            force("com.google.guava:guava:33.2.0-jre")
        }
    }
}

allprojects {
    configurations.all {
        resolutionStrategy {
            force("com.google.code.gson:gson:2.11.0")
            force("org.jdom:jdom2:2.0.6.1")
            force("commons-io:commons-io:2.16.1")
            force("com.google.protobuf:protobuf-javalite:3.25.5")
            force("com.google.protobuf:protobuf-java:3.25.5")
            force("io.netty:netty-handler:4.1.108.Final")
            force("io.netty:netty-codec-http2:4.1.108.Final")
            force("io.netty:netty-codec-http:4.1.108.Final")
            force("io.netty:netty-codec:4.1.108.Final")
            force("io.netty:netty-common:4.1.108.Final")
            force("org.bitbucket.b_c:jose4j:0.9.6")
            force("org.apache.commons:commons-compress:1.26.1")
            force("org.bouncycastle:bcprov-jdk18on:1.78.1")
            force("org.bouncycastle:bcpkix-jdk18on:1.78.1")
            force("com.squareup.okio:okio:3.9.0")
            force("com.google.guava:guava:33.2.0-jre")
        }
    }
}