#!/bin/sh
echo "mvn clean compile"
mvn clean compile

echo "mvn spring-boot:run -Pdev"
mvn spring-boot:run -Pdev
