FROM openjdk:11

RUN mkdir /app

COPY /target /app
RUN ls -l /app

WORKDIR /app

VOLUME /root/

EXPOSE 8080
EXPOSE 8081
EXPOSE 3306

CMD java -jar console-app-1.0-SNAPSHOT.jar com.company.Main
