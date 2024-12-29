FROM openjdk:17-jdk-slim
WORKDIR /app
COPY build/libs/wallet-app.jar wallet-app.jar
ENTRYPOINT ["java", "-jar", "wallet-app.jar"]