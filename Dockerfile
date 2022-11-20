FROM openjdk:17
COPY target/pedidos*SNAPSHOT.jar pedidos.jar
ENTRYPOINT ["java", "-jar", "/pedidos.jar"]
