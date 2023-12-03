#!/bin/bash

# Get the current directory of the script
CURRENT_DIR=$(dirname "${BASH_SOURCE[0]}")

# Navigate to the script's directory
cd "$CURRENT_DIR" || exit

# Go one directory up to the project root
cd ..

# Create a directory for compiled files if it doesn't exist
mkdir -p compiled

# Compile all Java files in the src directory and place the class files in the compiled directory
javac -d compiled src/*.java

# Check if the compilation was successful
if [ $? -eq 0 ]; then
    echo "Compilation successful."
    # Run the Java program (assuming the main class is YourJavaFile in the default package)
    java -cp compiled Main
else
    echo "Compilation failed. Check your code."
fi
