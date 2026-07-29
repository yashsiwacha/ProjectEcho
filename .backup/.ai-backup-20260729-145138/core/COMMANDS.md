# Common Commands

Repository Validation

git status
mvn -f backend/pom.xml test

Notes:
- The repository is Maven-only. No Gradle build file exists.
- There is no Maven wrapper (no mvnw, no .mvn/), so a local Maven
  install is required. .gitignore currently ignores .mvn/, which would
  exclude a wrapper if one were added. See DAR-0001 F-17.
- No module produces a bootable jar — every module sets
  spring-boot-maven-plugin <skip>true</skip>. See DAR-0001 F-05 / CR-005.

Documentation

tree docs -L 2
tree backend -L 2

Architecture

Read ADRs first.
Read ARBRs before modifying architecture.

Implementation

Architecture
↓

Review

↓

Founder Approval

↓

Implementation

↓

Testing
