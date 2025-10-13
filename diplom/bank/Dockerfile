FROM maven:3.9.9-eclipse-temurin-21 AS builder
WORKDIR /build

COPY pom.xml .
COPY bank/pom.xml bank/pom.xml

RUN sed -i '/<module>hotel<\/module>/d' pom.xml

RUN --mount=type=cache,target=/root/.m2 \
    mvn dependency:go-offline -B

COPY schemas schemas

COPY bank/src bank/src

RUN --mount=type=cache,target=/root/.m2 \
    mvn -pl bank clean package -DskipTests -B

FROM eclipse-temurin:21-jre-alpine
WORKDIR /bank
COPY --from=builder /build/bank/target/bank-*.jar bank.jar
ENTRYPOINT ["java", "-jar", "bank.jar"]