#!/bin/bash
# Script to fix and regenerate valid Gradle Wrapper files for Git tracking

echo "=== GANK SERVICE: Fixing Gradle Wrapper & Git Settings ==="

# 1. Regenerate wrapper
gradle wrapper --gradle-version 9.3.1

# 2. Grant permissions
chmod +x gradlew

# 3. Ensure binary tracking in Git
if [ ! -f .gitattributes ]; then
    echo "*.jar binary" > .gitattributes
    echo "gradlew text eol=lf" >> .gitattributes
fi

# 4. Force add wrapper files to git
git add -f gradle/wrapper/gradle-wrapper.jar
git add gradle/wrapper/gradle-wrapper.properties
git add gradlew
git add .gitattributes
git add .github/workflows/android-build.yml

echo "=== Done! Gradle wrapper regenerated & staged for commit ==="
echo "Run 'git commit -m \"Fix corrupt gradle wrapper and update CI workflow\"' and push to GitHub."
