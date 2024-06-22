# Estágio 1: Build da aplicação
FROM  maven:3.9.7-sapmachine-21 AS build

# Crie um diretório para a aplicação
WORKDIR /app

# Copie o arquivo pom.xml e o código fonte para o contêiner
COPY pom.xml .
COPY src ./src

# Execute o comando de build do Maven para criar o JAR
RUN mvn clean package

# Estágio 2: Imagem final
FROM groovy:jdk21-alpine

# Crie um diretório para a aplicação
WORKDIR /app

# Copie o JAR gerado no estágio de build para o contêiner
COPY --from=build /app/target/Bolsa-0.0.1-SNAPSHOT.jar app.jar

# Copie o banco de dados SQLite para o contêiner
COPY --chown=groovy:groovy /src/main/resources/banco.db /app/banco.db

# Defina as permissões do banco de dados para leitura e escrita
USER root
RUN chmod 666 /app/banco.db
USER groovy

# Defina a variável de ambiente para o caminho do banco de dados no contêiner
ENV DATABASE_URL jdbc:sqlite:/app/banco.db

# Expõe a porta que sua aplicação usa
EXPOSE 8080

# Defina o comando para rodar sua aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]