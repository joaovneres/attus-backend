# --- build stage ---
ARG MODULE=processos-consumidor
FROM eclipse-temurin:17-jdk-alpine AS build
WORKDIR /build
COPY mvnw .mvn pom.xml ./
RUN ./mvnw -B -pl $MODULE -am dependency:go-offline
COPY src ./src
RUN ./mvnw -B package -pl $MODULE -am -DskipTests

# --- runtime ---
FROM eclipse-temurin:17-jre-alpine
RUN adduser -D -g '' app
USER app
ARG MODULE=processos-consumidor
COPY --from=build /build/${MODULE}/target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
