FROM debian:bullseye-slim
RUN apt-get update
RUN apt-get install openjdk-17-jdk -y
RUN apt-get install maven -y