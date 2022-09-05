FROM openjdk:11

RUN mkdir /app
RUN mkdir /app/src
RUN mkdir /app/src/main
RUN mkdir /app/src/main/web-app

#COPY target/socket-server-1.0-SNAPSHOT.jar /app
COPY /target /app
COPY /src/main/java/com/company/Main.java /app
COPY /src/main/webapp /app/src/main/webapp
RUN ls -l /app

WORKDIR /app

VOLUME /root/

EXPOSE 8081
EXPOSE 3306

CMD java -jar servlets-app-1.0-SNAPSHOT.jar com.company.Main
