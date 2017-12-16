#!/bin/bash
git pull
mvn clean package -Dmaven.test.skip=true


docker rm -f reservation-pro
docker rmi -f reservation:pro
docker build -t reservation:pro .
docker run -d -p 50002:50002 --name reservation-pro reservation:pro java -Duser.timezone=GMT+8 -Dspring.profiles.active=pro -jar /app/app.jar
