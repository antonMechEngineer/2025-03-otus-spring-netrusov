FROM maven:3.9.9-eclipse-temurin-21 AS builder
WORKDIR /build

COPY pom.xml .
COPY hotel/pom.xml hotel/pom.xml

RUN sed -i '/<module>bank<\/module>/d' pom.xml

RUN --mount=type=cache,target=/root/.m2 \
    mvn dependency:go-offline -B

COPY schemas schemas
COPY hotel/src hotel/src

RUN --mount=type=cache,target=/root/.m2 \
    mvn -pl hotel clean package -DskipTests -B

FROM eclipse-temurin:21-jre-alpine
WORKDIR /hotel
COPY --from=builder /build/hotel/target/hotel-*.jar hotel.jar
ENTRYPOINT ["java", "-jar", "hotel.jar"]