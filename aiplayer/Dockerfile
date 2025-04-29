FROM gradle:8-jdk-21-and-22-alpine AS builder

WORKDIR /app

COPY --chown=gradle:gradle .. /app

# Construit uniquement le sous-projet core
RUN gradle :aiplayer:bootJar -x test

# Étape d'exécution
FROM openjdk:21-jdk-slim

WORKDIR /app

COPY --from=builder /app/aiplayer/build/libs/*.jar app.jar

# Copie du JAR généré depuis le builder (en supposant une application Spring Boot ou similaire)
EXPOSE 8080

CMD ["java", "-Xmx4G", "-jar", "app.jar"]
