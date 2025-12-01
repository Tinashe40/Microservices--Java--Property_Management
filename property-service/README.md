# Property Service

This service is a core component of the **Property Management System**, a microservices-based application. It is responsible for managing all property-related data, including properties, floors, and units.

## Features

- **Property Management**: Create, retrieve, update, and delete properties.
- **Floor Management**: Manage floors within a property.
- **Unit Management**: Handle units within a floor and property.
- **Statistics**: Provides detailed statistics for individual properties and system-wide analytics.
- **Search**: Powerful search capabilities for properties and units.
- **Authentication**: Secured endpoints using JWT.

## Prerequisites

Before you begin, ensure you have the following installed:

- Java 17 or later
- Maven 3.6 or later
- Docker and Docker Compose
- A running MySQL instance

## Database Setup

1.  Create a MySQL database named `properties`.
2.  The service will automatically create the necessary tables when it starts up, as defined by the JPA entities.

## Environment Variables

This service uses environment variables for configuration. You can set them in your operating system or in a `.env` file.

| Variable        | Description                               | Default Value              |
|-----------------|-------------------------------------------|----------------------------|
| `DB_USERNAME`   | The username for the MySQL database.      | `root`                     |
| `DB_PASSWORD`   | The password for the MySQL database.      | `sudo0047`                 |
| `JWT_SECRET`    | The secret key for signing JWT tokens.    | A default secret is provided. |
| `JWT_EXPIRATION`| The expiration time for JWT tokens in ms. | `604800000` (7 days)       |
| `EUREKA_URL`    | The URL of the Eureka discovery server.   | `http://localhost:8761/eureka` |

## Getting Started

To get the project on your local machine, clone the repository:

```bash
git clone https://github.com/your-username/property-management.git
cd property-management
```

## Building the Service

To build the service, run the following command from the `property-service` root directory:

```bash
./mvnw clean install
```

## Running the Service

After a successful build, you can run the service using:

```bash
./mvnw spring-boot:run
```

Alternatively, you can run the packaged application:

```bash
java -jar target/property-service-0.0.1-SNAPSHOT.jar
```

The service will start on the port configured in `src/main/resources/application.yml` (default is `8082`).

## API Endpoints

The following are the primary API endpoints exposed by the `property-service`.

### Property Management

**Base Path**: `/api/properties`

| Method | Endpoint             | Description                                          | Permissions |
|--------|----------------------|------------------------------------------------------|-------------|
| `POST` | `/`                  | Create a new property.                               | `property:create` |
| `GET`  | `/{id}`              | Get a property by its unique ID.                     | `property:read` (Admin or owner) |
| `GET`  | `/`                  | Get all properties with filtering and pagination.    | `property:read` (Admin sees all, User sees own) |
| `PUT`  | `/{id}`              | Update an existing property.                         | `property:update` (Admin or owner) |
| `DELETE`| `/{id}`              | Delete a property by its unique ID.                  | `property:delete` (Admin or owner) |
| `GET`  | `/search`            | Search for properties by name or address.            | `property:read` (Admin sees all, User sees own) |
| `GET`  | `/{id}/stats`        | Get detailed statistics for a specific property.     | `property:read` (Admin or owner) |
| `GET`  | `/count`             | Get the total number of properties in the system.    | `ADMIN`, `SUPER_ADMIN` |
| `GET`  | `/stats/system-wide` | Get system-wide statistics for all properties.       | `ADMIN`, `SUPER_ADMIN` |
| `GET`  | `/count-by-type`     | Get the total number of properties by type.          | `ADMIN`, `SUPER_ADMIN` |
| `GET`  | `/stats/system-wide-by-type` | Get system-wide statistics by property type. | `ADMIN`, `SUPER_ADMIN` |

### Floor Management

**Base Path**: `/api/floors`

| Method | Endpoint                  | Description                                      |
|--------|---------------------------|--------------------------------------------------|
| `POST` | `/`                       | Create a new floor within a property.            |
| `GET`  | `/{id}`                   | Get a floor by its unique ID.                    |
| `GET`  | `/`                       | Get all floors for a given property ID.          |
| `PUT`  | `/{id}`                   | Update an existing floor.                        |
| `DELETE`| `/{id}`                   | Delete a floor by its unique ID.                 |
| `GET`  | `/{id}/occupancy-stats`   | Get occupancy statistics for a specific floor.   |
| `POST` | `/{id}/refresh-occupancy` | Refresh the occupancy statistics for a floor.    |

### Unit Management

**Base Path**: `/api/units`

| Method  | Endpoint                        | Description                                          |
|---------|---------------------------------|------------------------------------------------------|
| `POST`  | `/`                             | Create a new unit within a property and floor.       |
| `GET`   | `/{id}`                         | Get a unit by its unique ID.                         |
| `GET`   | `/name/{name}`                  | Get a unit by its name and property ID.              |
| `GET`   | `/`                             | Get all units with filtering and pagination.         |
| `PUT`   | `/{id}`                         | Update an existing unit.                             |
| `DELETE`| `/{id}`                         | Delete a unit by its unique ID.                      |
| `PATCH` | `/{id}/occupancy`               | Update the occupancy status of a unit.               |
| `GET`   | `/search`                       | Search for units by name or tenant.                  |
| `GET`   | `/property/{propertyId}/income` | Calculate the potential rental income for a property.|
| `GET`   | `/property/{propertyId}/count`  | Count the total number of units in a property.       |

## Testing with Postman

This guide provides instructions on how to test the API endpoints using Postman. All requests should be made through the API Gateway.

### 1. Authentication

All endpoints require a valid **JSON Web Token (JWT)** for authentication. To obtain a token, you need to send a `POST` request to the `user-service` through the API gateway.

**Request:**

- **POST** `http://localhost:8080/api/auth/sign-in`
- **Headers**: `Content-Type: application/json`
- **Body**:
  ```json
  {
      "usernameOrEmail": "your-username",
      "password": "your-password"
  }
  ```

The response will contain a JWT token that you must include in the `Authorization` header for all subsequent requests.

### 2. Making Requests

**Base URL**: `http://localhost:8080/api`

For all requests to the `property-service`, you must include the following headers:

- `Content-Type`: `application/json` (for `POST` and `PUT` requests)
- `Authorization`: `Bearer <YOUR_JWT_TOKEN>`

Replace `<YOUR_JWT_TOKEN>` with the token you received from the authentication service.

---

### Example Requests

Here are a few examples of how to structure your requests in Postman.

#### Create a New Property

- **POST** `/properties`
- **Body**:
  ```json
  {
      "name": "Greenwood Plaza",
      "propertyType": "COMMERCIAL",
      "address": "123 Main St, Anytown, USA",
      "managedBy": 1
  }
  ```

#### Get Property by ID

- **GET** `/properties/1`

#### Create a New Floor

- **POST** `/floors`
- **Body**:
  ```json
  {
      "propertyId": 1,
      "name": "First Floor"
  }
  ```

#### Create a New Unit

- **POST** `/units`
- **Body**:
  ```json
  {
      "name": "Unit 101",
      "size": 100.5,
      "rentType": "FLAT",
      "monthlyRent": 1200.00,
      "occupancyStatus": "AVAILABLE",
      "propertyId": 1,
      "floorId": 1
  }
  ```

---

## Collaboration

I am open to collaborations! If you are interested in contributing to this project, please follow these steps:

1.  Fork the repository.
2.  Create a new branch for your feature or bug fix: `git checkout -b feature-name`
3.  Make your changes and commit them with a descriptive message.
4.  Push your changes to your forked repository.
5.  Open a pull request to the `main` branch of the original repository.

I will review your pull request as soon as possible. Thank you for your interest in collaborating!
