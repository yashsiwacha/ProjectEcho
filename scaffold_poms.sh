#!/bin/bash
set -e
BASE_DIR="/Users/yash/Yash-Workspace/projects/active/project-echo"
cd "$BASE_DIR"

MODULES=("echo-shared" "echo-identity" "echo-taxonomy" "echo-evidence" "echo-intelligence" "echo-mission" "echo-application")

for MODULE in "${MODULES[@]}"; do
    cat << POM > "$MODULE/pom.xml"
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <parent>
        <groupId>com.projectecho</groupId>
        <artifactId>project-echo-parent</artifactId>
        <version>1.0.0-SNAPSHOT</version>
    </parent>

    <artifactId>$MODULE</artifactId>
    <packaging>jar</packaging>

    <dependencies>
        <!-- Standard Spring Boot dependencies -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter</artifactId>
        </dependency>
POM

    if [ "$MODULE" == "echo-application" ]; then
        cat << POM >> "$MODULE/pom.xml"
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>com.projectecho</groupId>
            <artifactId>echo-shared</artifactId>
        </dependency>
        <dependency>
            <groupId>com.projectecho</groupId>
            <artifactId>echo-identity</artifactId>
        </dependency>
        <dependency>
            <groupId>com.projectecho</groupId>
            <artifactId>echo-taxonomy</artifactId>
        </dependency>
        <dependency>
            <groupId>com.projectecho</groupId>
            <artifactId>echo-evidence</artifactId>
        </dependency>
        <dependency>
            <groupId>com.projectecho</groupId>
            <artifactId>echo-intelligence</artifactId>
        </dependency>
        <dependency>
            <groupId>com.projectecho</groupId>
            <artifactId>echo-mission</artifactId>
        </dependency>
POM
    fi

    if [[ "$MODULE" != "echo-shared" && "$MODULE" != "echo-application" ]]; then
        cat << POM >> "$MODULE/pom.xml"
        <dependency>
            <groupId>com.projectecho</groupId>
            <artifactId>echo-shared</artifactId>
        </dependency>
POM
    fi

    cat << POM >> "$MODULE/pom.xml"
    </dependencies>
</project>
POM
done
echo "POMs scaffolded."
