from eclipse-temurin:21-jdk-jammy as build

workdir /build

copy src .

run chmod +X mvnw

run ./mvnw clean package -DskipTests

from eclipse-temurin:21-jre-jammy as runtime

workdir /app

run groupadd --system beluga
run useradd --system --gid beluga --home-dir /app beluga
run mkdir db && chown -R beluga:beluga db
run mkdir data && chown -R beluga:beluga data

copy --from=build --chown=beluga:beluga /build/beluga-scraper/target/beluga-scraper-1.0.jar /app/beluga.jar

env SPRING_PROFILES_ACTIVE=docker

user beluga

expose 8080

entrypoint ["java", "-jar", "/app/beluga.jar"]