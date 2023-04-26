FROM maven:3.8.7-amazoncorretto-17 AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package

FROM openjdk:17-jdk-slim
COPY --from=build /home/app/target/bitcoin-wallet-0.0.1-SNAPSHOT.jar /usr/local/lib/bitcoin-wallet.jar
EXPOSE 8080
CMD ["java", "-jar", "/usr/local/lib/bitcoin-wallet.jar"]