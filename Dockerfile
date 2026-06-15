FROM maven:3.8.8-eclipse-temurin-17 AS build
WORKDIR /workspace

# Copia pom e wrapper para cache das dependências
COPY pom.xml mvnw ./
COPY src ./src

# Build da aplicação (skip tests para acelerar, mudar se precisar)
RUN mvn -B -DskipTests package

FROM eclipse-temurin:17-jre
WORKDIR /app

RUN apt-get update \
    && apt-get install -y --no-install-recommends ca-certificates \
    && update-ca-certificates \
    && rm -rf /var/lib/apt/lists/*

# Copia jar construido
COPY --from=build /workspace/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]
