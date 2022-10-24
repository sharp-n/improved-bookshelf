# Use the OpenJDK 11 image as the base image
FROM openjdk:11

# Create a new app directory for my application files
RUN mkdir /app
#RUN mkdir /app/src
#RUN mkdir /app/src/main
#RUN mkdir /app/src/main/webapp

# Copy the app files from host machine to image filesystem
COPY all-in-one/src/main/java/com/company/ /app
COPY all-in-one/target/classes/com/company/ /app
COPY all-in-one/target/all-in-one-1.0-SNAPSHOT.jar /app
#COPY servlets-app/src/main/webapp /app/src/main/webapp
RUN ls -l /app

#Set the directory for executing future commands
WORKDIR /app

VOLUME /root/

EXPOSE 8080
EXPOSE 8081
EXPOSE 3306
EXPOSE 3307
EXPOSE 3308


CMD java -jar all-in-one-1.0-SNAPSHOT.jar
