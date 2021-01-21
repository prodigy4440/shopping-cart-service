# Sello-Backend

How to start the Sello-Backend application
---

1. Run `mvn clean install` to build your application
1. Start application with `java -jar target/ShoppigCartService-1.0-SNAPSHOT.jar server config.yml`
1. To check that your application is running enter url `http://localhost:8080`

Health Check
---

To see your applications health enter url `http://localhost:8081/healthcheck`


Running full application
---

Start With:: docker-compose up -d

Stop With::  docker-compose down --remove-orphans

Swagger Documentation
---

http://localhost:8080/swagger
