FROM java:8-jre
COPY config.yml /opt/shopper/
COPY target/shopping-order-service-1.0-SNAPSHOT-sources.jar /opt/shopper/
EXPOSE 8080
WORKDIR /opt/shopper
CMD ["java","-jar", "shopping-order-service-1.0-SNAPSHOT-sources.jar", "server", "config.yml"]