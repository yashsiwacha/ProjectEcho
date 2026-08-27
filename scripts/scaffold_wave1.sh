#!/bin/bash
set -e

# Base directory
BASE_DIR="/Users/yash/Yash-Workspace/projects/active/project-echo"
cd "$BASE_DIR"

# Modules
MODULES=("echo-shared" "echo-identity" "echo-taxonomy" "echo-evidence" "echo-intelligence" "echo-mission" "echo-application")

# Create root directories
mkdir -p config/checkstyle config/pmd .github/workflows

# Scaffold modules
for MODULE in "${MODULES[@]}"; do
    # Convert dash to dot for package name (e.g., echo-shared -> echo.shared, wait, just use the second part for simplicity)
    PKG_NAME=$(echo $MODULE | cut -d'-' -f2)
    PKG_PATH="src/main/java/com/projectecho/$PKG_NAME"
    TEST_PKG_PATH="src/test/java/com/projectecho/$PKG_NAME"
    
    mkdir -p "$MODULE/$PKG_PATH/domain"
    mkdir -p "$MODULE/$PKG_PATH/application"
    mkdir -p "$MODULE/$PKG_PATH/infrastructure"
    mkdir -p "$MODULE/$PKG_PATH/presentation"
    
    mkdir -p "$MODULE/$TEST_PKG_PATH"
    
    if [ "$MODULE" != "echo-shared" ] && [ "$MODULE" != "echo-application" ]; then
        mkdir -p "$MODULE/src/main/resources/db/migration/$MODULE"
        touch "$MODULE/src/main/resources/db/migration/$MODULE/V1__init.sql"
    fi
    
    # Empty README
    echo "# $MODULE" > "$MODULE/README.md"
done

# Specifically for echo-application
mkdir -p echo-application/src/test/java/com/projectecho/architecture
mkdir -p echo-application/src/main/resources

echo "Scaffolding complete."
