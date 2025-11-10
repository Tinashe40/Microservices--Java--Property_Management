# Eureka Server

This service is a critical component of the Property Management System, providing service discovery capabilities to all microservices in the ecosystem.

## Description

The Eureka Server is a service registry that allows microservices to locate each other without hardcoding hostnames and ports. All services register themselves with the Eureka Server and send periodic heartbeats to renew their leases. This enables dynamic scaling, resilience, and simplifies the overall architecture.

## Getting Started

To get the project on your local machine, you can clone the repository using the following command:

```bash
git clone https://github.com/your-username/property-management.git
```

*Note: Replace `https://github.com/your-username/property-management.git` with the actual URL of your repository.*

## Building

To build the service, run the following command from the service's root directory (`eureka-server`):

```bash
mvn clean install
```

This will compile the code, run the tests, and package the application into a JAR file.

## Running the Service

After a successful build, you can run the service using:

```bash
java -jar target/eureka-server-0.0.1-SNAPSHOT.jar
```

The service will start on port 8761 as configured in `src/main/resources/application.yml`.

## Eureka Dashboard

Once the server is running, you can access the Eureka dashboard in your browser to see the registered services.

**URL:** [http://localhost:8761/](http://localhost:8761/)

## Actuator Endpoints

This service uses Spring Boot Actuator to expose management and monitoring endpoints. The following endpoints are available:

- **Health:** [http://localhost:8761/actuator/health](http://localhost:8761/actuator/health)
- **Metrics:** [http://localhost:8761/actuator/metrics](http://localhost:8761/actuator/metrics)
- **Prometheus:** [http://localhost:8761/actuator/prometheus](http://localhost:8761/actuator/prometheus)

## Configuration

Configuration for the service can be found in `src/main/resources/application.yml`. The following properties are configured:

- **`spring.application.name`:** The name of the application.
- **`server.port`:** The port on which the server will run.
- **`eureka.client.register-with-eureka`:** Whether the server should register itself with Eureka.
- **`eureka.client.fetch-registry`:** Whether the server should fetch the registry from Eureka.
- **`eureka.server.wait-time-in-ms-when-sync-empty`:** The time to wait before sending a response when the registry is empty.
- **`management.endpoints.web.exposure.include`:** Which actuator endpoints to expose.
- **`management.endpoint.health.show-details`:** Whether to show details in the health endpoint.
- **`logging.level.com.proveritus.eurekaserver`:** The logging level for the application.

## Collaboration

I am open to collaborations! If you are interested in contributing to this project, please follow these steps:

1.  Fork the repository.
2.  Create a new branch for your feature or bug fix: `git checkout -b feature-name`
3.  Make your changes and commit them with a descriptive message.
4.  Push your changes to your forked repository.
5.  Open a pull request to the `main` branch of the original repository.

I will review your pull request as soon as possible. Thank you for your interest in collaborating!
