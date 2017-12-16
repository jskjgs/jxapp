#!/bin/bash
git pull
mvn clean package -Dmaven.test.skip=true


docker rm -f reservation-test
docker rmi -f reservation:test
docker build -t reservation:test .
docker run -d -p 50002:50002 --name reservation-test reservation:test java -Duser.timezone=GMT+8 -Dspring.profiles.active=test -jar /app/app.jar
