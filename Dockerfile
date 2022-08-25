# Use the OpenJDK 11 image as the base image
FROM openjdk:11

# Create a new app directory for my application files
RUN mkdir /app
RUN mkdir /app/src
RUN mkdir /app/src/main
RUN mkdir /app/src/main/webapp

# Copy the app files from host machine to image filesystem
COPY src/main/java/com/company/ /app
COPY target/bookshelf-1.0-SNAPSHOT.jar /app
COPY src/main/webapp /app/src/main/webapp
RUN ls -l /app

#Set the directory for executing future commands
WORKDIR /app

VOLUME /root/

EXPOSE 8080
EXPOSE 8081


CMD java -jar bookshelf-1.0-SNAPSHOT.jar
