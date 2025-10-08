FROM maven:3.9.9-eclipse-temurin-21 AS builder
WORKDIR /build
COPY . .
RUN mvn -pl bank clean package -DskipTests

FROM eclipse-temurin:21-jre-alpine
WORKDIR /bank
COPY --from=builder /build/bank/target/bank-*.jar bank.jar
ENTRYPOINT ["java", "-jar", "bank.jar"] 