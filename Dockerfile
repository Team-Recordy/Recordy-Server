FROM amd64/amazoncorretto:17
WORKDIR /app
COPY ./build/libs/server-0.0.1-SNAPSHOT.jar /app/server.jar
CMD ["java", "-Duser.timezone=Asia/Seoul", "-Dspring.profiles.active=dev", "-Xmx250m", "-Xms250m", "-jar", "server.jar"]