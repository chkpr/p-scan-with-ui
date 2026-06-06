# p-scan

A Java Spring Boot web application that scans TCP ports on a target host.
Built as a learning project to explore Java concurrency, network programming, and REST APIs.

## Tech stack

- Java 17
- Spring Boot
- HTML / CSS / JavaScript

## How to run

**Prerequisites:** 

Java 17+, Maven 3.6+

```bash
git clone https://github.com/chkpr/p-scan-with-ui.git
cd p-scan-with-ui
mvn spring-boot:run
```

Then open `http://localhost:8080` in your browser.

## Concepts illustrated

- TCP port scanning with `java.net.Socket`
- Parallel scanning with `ExecutorService` and `ConcurrentLinkedQueue`
- REST API with Spring Boot `@RestController`
- Frontend/backend communication with `fetch` API
