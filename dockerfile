FROM maven:3-amazoncorretto-17 AS build

WORKDIR /opt/myapp
COPY pom.xml .
COPY ./src ./src
RUN mvn package

FROM amazoncorretto:17-alpine
WORKDIR /opt/myapp
COPY --from=build /opt/myapp/target/ROOT.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
