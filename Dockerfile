FROM openjdk:17-alpine
#RUN addgroup -S spring && adduser -S spring -G spring
#USER spring:spring
COPY target/pedidos*SNAPSHOT.jar pedidos.jar
ENTRYPOINT ["java", "-jar", "/pedidos.jar"]
