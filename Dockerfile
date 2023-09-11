FROM eclipse-temurin:17-jdk-alpine AS stage1
WORKDIR /opt/app
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN sed -i 's/\r$//' mvnw
RUN chmod +x mvnw
RUN ./mvnw dependency:go-offline
COPY ./src ./src
RUN ./mvnw clean package -DskipTests

FROM  eclipse-temurin:17-jre-alpine
WORKDIR /opt/app
COPY --from=stage1 /opt/app/target/*.jar /opt/app/app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/opt/app/app.jar"]