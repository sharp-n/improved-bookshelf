# Use the OpenJDK 11 image as the base image
FROM openjdk:11

# Create a new app directory for my application files
RUN mkdir /app

# Copy the app files from host machine to image filesystem
COPY src/main/java/com/company/ /app
COPY target/bookshelf-1.0-SNAPSHOT.jar /app

RUN ls -l /app

#Set the directory for executing future commands
WORKDIR /app

VOLUME /root/

EXPOSE 8080
CMD java -jar bookshelf-1.0-SNAPSHOT.jar
