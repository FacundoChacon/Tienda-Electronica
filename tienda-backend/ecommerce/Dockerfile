# Etapa 1: build - compila el proyecto con Maven y genera el .jar
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app

# Copiamos primero solo el pom.xml para aprovechar el cache de Docker:
# si las dependencias no cambiaron, Docker no las vuelve a descargar
# en cada build, aunque el codigo Java si haya cambiado.
COPY pom.xml .
RUN mvn dependency:go-offline -B

COPY src ./src
RUN mvn clean package -DskipTests -B

# Etapa 2: runtime - imagen final, liviana, solo con el JRE y el .jar compilado
# (no incluye Maven ni el codigo fuente, solo lo necesario para ejecutar la app).
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

# Render inyecta su propia variable de entorno PORT; application.properties
# ya esta configurado para leerla (server.port=${PORT:8080}).
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
