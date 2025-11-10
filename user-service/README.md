# user-service

This service is part of the Property Management System.

### Description

This service manages user-related operations, such as user registration, authentication, and profile management.

### Getting Started

To get the project on your local machine, you can clone the repository using the following command:

```bash
git clone https://github.com/your-username/property-management.git
```

### Building

To build the service, run the following command from the service's root directory (`user-service`):

```bash
mvn clean install
```

### Running the Service

After a successful build, you can run the service using:

```bash
java -jar target/user-service-0.0.1-SNAPSHOT.jar
```

The service will start on the port configured in `src/main/resources/application.yml` (default is usually 8080).

### API Endpoints

The following are the API endpoints exposed by the `user-service`.

#### Authentication Endpoints

Base Path: `/api/auth`

| Method | Endpoint        | Description                               |
|--------|-----------------|-------------------------------------------|
| POST   | `/sign-in`      | Authenticates a user and returns a JWT token. |
| POST   | `/signup`       | Registers a new user.                     |
| GET    | `/me`           | Gets the current user.                    |
| GET    | `/validate-token` | Validates a JWT token.                  |

#### User Management Endpoints

Base Path: `/api/users`

| Method | Endpoint          | Description                     |
|--------|-------------------|---------------------------------|
| GET    | `/`               | Get all users (Admin only).     |
| GET    | `/{id}`           | Get user by ID.                 |
| GET    | `/by-username`    | Get user by username.           |
| POST   | `/by-ids`         | Get users by IDs.               |
| DELETE | `/{id}`           | Delete user (Admin only).       |
| POST   | `/`               | Create a new user (Admin only). |
| PUT    | `/{id}`           | Update a user.                  |
| POST   | `/{id}/roles`     | Assign roles to a user.         |
| PUT    | `/{id}/deactivate`| Deactivate a user.              |
| POST   | `/change-password`| Change current user password.   |
| POST   | `/{id}/reset-password`| Reset user password.        |

#### Role Management Endpoints

Base Path: `/api/roles`

| Method | Endpoint        | Description                               |
|--------|-----------------|-------------------------------------------|
| GET    | `/`             | Get all available roles.                  |
| POST   | `/`             | Create a new role.                        |
| GET    | `/{id}`         | Get role by ID.                         |
| PUT    | `/{id}`         | Update a role.                          |
| DELETE | `/{id}`         | Delete a role.                          |

#### Permission Management Endpoints

Base Path: `/api/permissions`

| Method | Endpoint        | Description                               |
|--------|-----------------|-------------------------------------------|
| GET    | `/`             | Get all available permissions.            |

### Default Roles and Permissions

The system comes with the following default roles and their initial permissions:

*   **SUPER_ADMIN**: Has all available permissions. This role is assigned to the initially seeded admin user.
*   **ADMIN**: Has all available permissions.
*   **USER**: Has `user:read` permission.

---

### Postman Guide

This guide provides instructions on how to test the API endpoints using Postman.

**Base URL**: `http://localhost:8081/api`

**Important**: For all `POST` and `PUT` requests with a request body, make sure to set the `Content-Type` header to `application/json`.

#### Authentication

##### 1. Sign Up

*   **POST** `/auth/signup`
*   **Body**:
    ```json
    {
        "username": "testuser",
        "email": "testuser@example.com",
        "password": "Password@123",
        "firstName": "Test",
        "lastName": "User"
    }
    ```

##### 2. Sign In

*   **POST** `/auth/sign-in`
*   **Body** (for admin user):
    ```json
    {
        "usernameOrEmail": "Tinashe40",
        "password": "sudo0047"
    }
    ```
*   **Note**: A successful sign-in will return a JWT token. Copy this token to use in the `Authorization` header for protected endpoints.

##### 3. Get Current User

*   **GET** `/auth/me`
*   **Headers**: `Authorization: Bearer <YOUR_JWT_TOKEN>`

#### User Management

**Note**: For the following requests, you need to have a valid JWT token from an authenticated user with the required permissions. Use the token from the admin user for admin operations.

##### 1. Get All Users

*   **GET** `/users`
*   **Headers**: `Authorization: Bearer <ADMIN_JWT_TOKEN>`

##### 2. Get User by ID

*   **GET** `/users/{id}`
*   **Headers**: `Authorization: Bearer <ADMIN_JWT_TOKEN>`

##### 3. Create User

*   **POST** `/users`
*   **Headers**: `Authorization: Bearer <ADMIN_JWT_TOKEN>`
*   **Body**:
    ```json
    {
        "username": "newuser",
        "email": "newuser@example.com",
        "password": "Password@123",
        "firstName": "New",
        "lastName": "User",
        "roles": ["USER"]
    }
    ```

##### 4. Update User

*   **PUT** `/users/{id}`
*   **Headers**: `Authorization: Bearer <ADMIN_JWT_TOKEN>`
*   **Body**:
    ```json
    {
        "email": "updateduser@example.com",
        "firstName": "Updated First",
        "lastName": "Updated Last",
        "enabled": true,
        "accountNonExpired": true,
        "accountNonLocked": true,
        "credentialsNonExpired": true
    }
    ```

##### 5. Assign Roles to User

*   **POST** `/users/{id}/roles`
*   **Headers**: `Authorization: Bearer <ADMIN_JWT_TOKEN>`
*   **Body**:
    ```json
    [
        "ADMIN",
        "SUPER_ADMIN",
        "USER"
    ]
    ```
*   **Note**: Available roles are `ADMIN`, `SUPER_ADMIN`, and `USER`.

##### 6. Delete User

*   **DELETE** `/users/{id}`
*   **Headers**: `Authorization: Bearer <ADMIN_JWT_TOKEN>`

#### Role Management

##### 1. Get All Roles

*   **GET** `/roles`
*   **Headers**: `Authorization: Bearer <ADMIN_JWT_TOKEN>`

##### 2. Create Role

*   **POST** `/roles`
*   **Headers**: `Authorization: Bearer <ADMIN_JWT_TOKEN>`
*   **Body**:
    ```json
    {
        "name": "MANAGER",
        "permissions": [
            "user:read",
            "user:update"
        ]
    }
    ```

##### 3. Get Role by ID

*   **GET** `/roles/{id}`
*   **Headers**: `Authorization: Bearer <ADMIN_JWT_TOKEN>`

##### 4. Update Role

*   **PUT** `/roles/{id}`
*   **Headers**: `Authorization: Bearer <ADMIN_JWT_TOKEN>`
*   **Body**:
    ```json
    {
        "name": "MANAGER",
        "permissions": [
            "user:read",
            "user:update",
            "user:create"
        ]
    }
    ```

##### 5. Delete Role

*   **DELETE** `/roles/{id}`
*   **Headers**: `Authorization: Bearer <ADMIN_JWT_TOKEN>`

#### Permission Management

##### 1. Get All Permissions

*   **GET** `/permissions`
*   **Headers**: `Authorization: Bearer <ADMIN_JWT_TOKEN>`

### Configuration

Configuration for the service can be found in `src/main/resources/application.yml`.

### Collaboration

I am open to collaborations! If you are interested in contributing to this project, please follow these steps:

1.  Fork the repository.
2.  Create a new branch for your feature or bug fix: `git checkout -b feature-name`
3.  Make your changes and commit them with a descriptive message.
4.  Push your changes to your forked repository.
5.  Open a pull request to the `main` branch of the original repository.

I will review your pull request as soon as possible. Thank you for your interest in collaborating!