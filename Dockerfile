FROM eclipse-temurin:21-jre
VOLUME /tmp
COPY target/book-lovers-0.0.1-SNAPSHOT.jar book-lovers.jar
ENTRYPOINT ["java","-jar","/app.jar", "--spring.profiles.active=prod"]