FROM openjdk:8

RUN mkdir /app
RUN mkdir /app/src
RUN mkdir /app/src/main
RUN mkdir /app/src/main/resources

#COPY target/socket-server-1.0-SNAPSHOT.jar /app
COPY /target/springboot-app-0.0.1-SNAPSHOT.jar /app
COPY /src/main/java/com/company/Main.java /app
COPY /src/main/resources /app/src/main/resources
RUN ls -l /app

WORKDIR /app

VOLUME /root/

EXPOSE 8090

CMD java -jar springboot-app-0.0.1-SNAPSHOT.jar com.company.Main
