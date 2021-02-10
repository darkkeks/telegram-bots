FROM openjdk:11-jdk-slim AS build
WORKDIR /build/

# Run gradlew with only wrapper files to cache binary
COPY gradlew /build/
COPY gradle /build/gradle
RUN ./gradlew --version

COPY . /build
RUN ./gradlew build -x test --no-daemon


FROM openjdk:11-jdk-slim
EXPOSE 8080
RUN mkdir /app

# expecting a single jar
COPY --from=build /build/kks-stat/build/libs/*.jar /app/app.jar

ENTRYPOINT ["java", "-Xmx512M", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/app/app.jar"]
