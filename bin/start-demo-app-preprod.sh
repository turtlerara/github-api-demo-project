#!/bin/bash
./gradlew bootJar
java -jar ./demo-app/build/libs/demo-app-1.0.jar -Dspring.profiles.active=preprod
