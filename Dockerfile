FROM maven:3-jdk-8-slim

RUN apt update && apt install -y vim make

WORKDIR /app

COPY . /app

CMD make install ci