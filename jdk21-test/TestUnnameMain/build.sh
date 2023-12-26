#!/bin/bash
JDK21_HOME=/usr/java/jdk-21.0.1+12

${JDK21_HOME}/bin/javac --release 21 --enable-preview HelloWorld.java
