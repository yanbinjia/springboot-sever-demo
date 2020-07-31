#!/bin/sh
echo "mvn clean compile"
mvn clean compile -Dmaven.test.skip=true

echo "mvn spring-boot:run -Pdev"
mvn spring-boot:run -Pdev -Dmaven.test.skip=true
