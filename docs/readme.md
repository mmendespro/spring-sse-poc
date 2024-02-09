# Spring Server-Sent Events

This project is a sample application with Server-Sent Events (SSE) support.

## About SSE
A server-sent event is when a web page automatically gets updates from a server. This was also possible before, but the web page would have to ask if any updates were available. With server-sent events, the updates come automatically.

![sse](./docs/img/sse.png)

## Quickstart

From the command line do:

```
git clone https://github.com/mmendespro/spring-sse-poc.git
cd spring-sse-poc
mvn clean package
java -jar target/spring-sse-poc-0.0.1-SNAPSHOT.jar
```