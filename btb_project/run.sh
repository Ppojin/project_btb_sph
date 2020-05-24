#!/bin/sh
echo "======================= btb ======================="
cd btb
mvn clean
mvn compile
mvn install

echo "======================= exam ======================="
cd ../exam
mvn clean
mvn compile
mvn install

echo "======================= qa ======================="
cd ../qa
mvn clean
mvn compile
mvn install

echo "======================= result ======================="
cd ../result
mvn clean
mvn compile
mvn install

# docker-compose up
