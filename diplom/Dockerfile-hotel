FROM maven:3.9.9-eclipse-temurin-21 AS builder
WORKDIR /build
COPY . .

RUN mvn -pl hotel clean package -DskipTests

FROM eclipse-temurin:21-jre-alpine
WORKDIR /hotel
COPY --from=builder /build/hotel/target/hotel-*.jar hotel.jar
ENTRYPOINT ["java", "-jar", "hotel.jar"]