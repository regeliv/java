from maven:3.9-eclipse-temurin-25 as build
workdir /app
copy pom.xml .
run mvn --quiet --batch-mode dependency:go-offline
copy src ./src
run mvn --quiet --batch-mode package --define skipTests

from eclipse-temurin:25-jre
workdir /app
copy --from=build /app/target/*.jar app.jar
expose 8080
entrypoint ["java", "-jar", "app.jar"]
