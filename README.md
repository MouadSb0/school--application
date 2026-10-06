# Workshop REST API

> A Java web application for managing students in a school context, combining a classic Servlet/JSP web layer with a Jakarta REST (Jersey) API and MySQL persistence.

[![Java](https://img.shields.io/badge/Java-17-orange)](https://openjdk.org/projects/jdk/17/)
[![Jakarta Servlet](https://img.shields.io/badge/Jakarta%20Servlet-6.0-blue)](https://jakarta.ee/specifications/servlet/6.0/)
[![Jersey](https://img.shields.io/badge/Jersey-3.x-purple)](https://eclipse-ee4j.github.io/jersey/)
[![MySQL](https://img.shields.io/badge/MySQL-8.x-4479A1)](https://www.mysql.com/)
[![Build](https://img.shields.io/badge/build-Maven-C71A36)](https://maven.apache.org/)
[![License](https://img.shields.io/badge/license-Educational-lightgrey)](#license)

---

## Table of Contents

- [Overview](#overview)
- [Features](#features)
- [Tech Stack](#tech-stack)
- [Architecture](#architecture)
- [Project Structure](#project-structure)
- [Getting Started](#getting-started)
  - [Prerequisites](#prerequisites)
  - [1. Clone the Repository](#1-clone-the-repository)
  - [2. Create the Database](#2-create-the-database)
  - [3. Configure the Datasource](#3-configure-the-datasource)
  - [4. Build the Project](#4-build-the-project)
  - [5. Deploy to Tomcat](#5-deploy-to-tomcat)
- [Running the Application](#running-the-application)
- [REST API Reference](#rest-api-reference)
- [Web Interface](#web-interface)
- [Troubleshooting](#troubleshooting)
- [Possible Improvements](#possible-improvements)
- [License](#license)
- [Author](#author)

---

## Overview

**Workshop REST API** is a training-oriented Java EE project that shows how the same domain model can be exposed through two different entry points:

1. A **server-rendered web interface** built with Jakarta Servlets and JSP/JSTL.
2. A **JSON REST API** built with Jakarta REST (Jersey).

Both layers share the same DAO and model classes and persist data to a MySQL database through a JNDI datasource. The project is deliberately small, but it follows an MVC-inspired separation of concerns so it can serve as a template for larger data-driven applications.

### Learning Objectives

By exploring this project you will see how to:

- Connect a Java web application to a MySQL database via a JNDI datasource.
- Build a CRUD interface for students using Servlets and JSP.
- Expose the same data as a REST API using Jersey.
- Organize a Java web project with `controllers`, `dao`, `models`, `resources`, and `util` packages.
- Package and deploy a WAR application on Apache Tomcat.

---

## Features

- 📋 List all students
- ➕ Add a new student through a browser form
- 🔎 Retrieve a single student by ID via the REST API
- 🔄 Create, update, and delete student records via REST endpoints
- 🗄️ MySQL-backed persistence using a JNDI datasource
- 🖥️ JSP/JSTL presentation layer for the web interface
- 🧩 Clean separation between web layer, REST layer, DAO, and model

---

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 17 |
| Web Layer | Jakarta Servlet 6.0, JSP, JSTL |
| REST Layer | Jakarta REST (Jersey 3.x) |
| Persistence | JDBC, MySQL Connector/J |
| Database | MySQL 8.x |
| Build Tool | Maven |
| Runtime | Apache Tomcat 10+ (or any Servlet 6.0-compatible container) |

> ⚠️ **Important:** This project targets **Jakarta EE 10** (`jakarta.*` namespace). It will **not** run on Tomcat 9 or earlier, which use the legacy `javax.*` namespace.

---

## Architecture

```
┌──────────────────────┐        ┌──────────────────────┐
│   Browser (JSP)      │        │   REST Client        │
│   /apprenants        │        │   /api/apprenants    │
└──────────┬───────────┘        └──────────┬───────────┘
           │                               │
           ▼                               ▼
┌──────────────────────┐        ┌──────────────────────┐
│  ApprenantServlet    │        │  ApprenantsResource  │
│  (Controller)        │        │  (JAX-RS Resource)   │
└──────────┬───────────┘        └──────────┬───────────┘
           │                               │
           └───────────┬───────────────────┘
                       ▼
              ┌──────────────────┐
              │   ApprenantDao   │
              │   (DAO Layer)    │
              └────────┬─────────┘
                       ▼
              ┌──────────────────┐
              │ ConnectionFactory│  →  JNDI Datasource
              └────────┬─────────┘
                       ▼
              ┌──────────────────┐
              │   MySQL Server   │
              │   gestion_ecole  │
              └──────────────────┘
```

---

## Project Structure

```text
workshop-rest-api/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── ma/youcode/workshop/
│   │   │       ├── controllers/     # Servlets (web layer)
│   │   │       ├── dao/             # Data access objects
│   │   │       ├── models/          # Domain entities
│   │   │       ├── resources/       # JAX-RS resources (REST layer)
│   │   │       └── util/            # ConnectionFactory and helpers
│   │   ├── webapp/
│   │   │   ├── META-INF/            # context.xml (JNDI datasource)
│   │   │   ├── WEB-INF/             # web.xml, JSP views, libs
│   │   │   ├── css/                 # Stylesheets
│   │   │   ├── index.jsp            # Entry point
│   │   │   └── ...
│   └── test/                        # Unit tests
├── pom.xml
├── README.md
└── target/                          # Build output (generated)
```

### Main Components

| Component | Responsibility |
|---|---|
| `ApprenantServlet` | Handles browser requests for the student management page |
| `ApprenantsResource` | Exposes REST endpoints under `/api/apprenants` |
| `ApprenantDao` | Performs CRUD operations against the database |
| `Apprenant` | Student model / entity |
| `ConnectionFactory` | Centralizes JNDI datasource lookups and connection creation |

---

## Getting Started

### Prerequisites

Ensure the following are installed and running before you begin:

| Requirement | Version |
|---|---|
| JDK | 17 or later |
| Maven | 3.8+ |
| MySQL Server | 8.x |
| Apache Tomcat | 10.0+ (Jakarta EE 10 compatible) |

Verify your environment:

```bash
java -version
mvn -version
mysql --version
```

---

### 1. Clone the Repository

```bash
git clone <repository-url>
cd workshop-rest-api
```

---

### 2. Create the Database

Start your MySQL server and create the database and table:

```sql
CREATE DATABASE IF NOT EXISTS gestion_ecole
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE gestion_ecole;

CREATE TABLE IF NOT EXISTS etudiant (
    id      INT PRIMARY KEY AUTO_INCREMENT,
    nom     VARCHAR(100) NOT NULL,
    prenom  VARCHAR(100) NOT NULL,
    email   VARCHAR(150),
    filiere VARCHAR(100)
);
```

Optional sample data:

```sql
INSERT INTO etudiant (nom, prenom, email, filiere) VALUES
    ('Martin', 'Alice', 'alice.martin@example.com', 'Informatique'),
    ('Benali', 'Youssef', 'youssef.benali@example.com', 'Réseaux');
```

---

### 3. Configure the Datasource

The application uses a **JNDI datasource** declared in `src/main/webapp/META-INF/context.xml`:

```xml
<Context>
    <Resource name="jdbc/GestionEcoleDS"
              auth="Container"
              type="javax.sql.DataSource"
              driverClassName="com.mysql.cj.jdbc.Driver"
              url="jdbc:mysql://localhost:3306/gestion_ecole?useSSL=false"
              username="root"
              password=""/>
</Context>
```

| Attribute | Description |
|---|---|
| `name` | JNDI name used by `ConnectionFactory` — must match the lookup string in the code |
| `driverClassName` | MySQL Connector/J driver class |
| `url` | JDBC connection URL (host, port, database, options) |
| `username` / `password` | MySQL credentials |

> 💡 **Tip:** For production deployments, avoid hardcoding credentials. Prefer environment-specific `context.xml` files or container-managed configuration.

---

### 4. Build the Project

```bash
mvn clean package
```

This produces a WAR file at:

```
target/workshop-connection-db.war
```

---

### 5. Deploy to Tomcat

**Option A — Manual deployment**

```bash
cp target/workshop-connection-db.war $CATALINA_HOME/webapps/
$CATALINA_HOME/bin/startup.sh      # or startup.bat on Windows
```

**Option B — Tomcat Manager**

Upload the WAR through the Tomcat Manager web UI at `http://localhost:8080/manager`.

**Option C — IDE deployment**

Configure a Tomcat 10+ server in IntelliJ IDEA or Eclipse and deploy the artifact directly.

---

## Running the Application

Once deployed, the application is available at the context path `/workshop-connection-db`:

| Entry Point | URL |
|---|---|
| Web interface | `http://localhost:8080/workshop-connection-db/apprenants` |
| REST API base | `http://localhost:8080/workshop-connection-db/api/apprenants` |

---

## REST API Reference

Base URL:

```
http://localhost:8080/workshop-connection-db/api
```

### Endpoint Summary

| Method | Endpoint | Description | Request Body | Typical Response |
|---|---|---|---|---|
| `GET` | `/apprenants` | Retrieve all students | — | `200 OK` |
| `GET` | `/apprenants/{id}` | Retrieve one student by ID | — | `200 OK` / `404 Not Found` |
| `POST` | `/apprenants` | Create a new student | JSON | `201 Created` |
| `PUT` | `/apprenants/{id}` | Update an existing student | JSON | `200 OK` / `404 Not Found` |
| `DELETE` | `/apprenants/{id}` | Delete a student | — | `204 No Content` / `404 Not Found` |

> Status codes follow standard REST conventions. Confirm the exact behaviour against your implementation.

### Student Representation

```json
{
  "id": 1,
  "nom": "Martin",
  "prenom": "Alice",
  "email": "alice.martin@example.com",
  "filiere": "Informatique"
}
```

### Examples

**Get all students**

```bash
curl -X GET http://localhost:8080/workshop-connection-db/api/apprenants
```

**Get a student by ID**

```bash
curl -X GET http://localhost:8080/workshop-connection-db/api/apprenants/1
```

**Create a student**

```bash
curl -X POST http://localhost:8080/workshop-connection-db/api/apprenants \
  -H "Content-Type: application/json" \
  -d '{
        "nom": "Martin",
        "prenom": "Alice",
        "email": "alice.martin@example.com",
        "filiere": "Informatique"
      }'
```

**Update a student**

```bash
curl -X PUT http://localhost:8080/workshop-connection-db/api/apprenants/1 \
  -H "Content-Type: application/json" \
  -d '{
        "nom": "Martin",
        "prenom": "Alice",
        "email": "alice.martin@newdomain.com",
        "filiere": "Génie Logiciel"
      }'
```

**Delete a student**

```bash
curl -X DELETE http://localhost:8080/workshop-connection-db/api/apprenants/1
```

---

## Web Interface

In addition to the REST API, the project exposes a servlet-based page for browsing and creating students through a browser form:

```
http://localhost:8080/workshop-connection-db/apprenants
```

This layer is useful for quick manual testing and classroom demonstrations, and it shares the exact same DAO and model classes as the REST API.

---

## Troubleshooting

| Symptom | Likely Cause | Fix |
|---|---|---|
| `404` on `/api/apprenants` | Jersey servlet not registered or wrong context path | Check the Jersey servlet mapping in `web.xml` or the `@ApplicationPath` annotation |
| `ClassNotFoundException: com.mysql.cj.jdbc.Driver` | MySQL Connector/J missing from the WAR | Confirm the dependency in `pom.xml` is not `provided` |
| `NameNotFoundException: jdbc/GestionEcoleDS` | JNDI name mismatch or `context.xml` not deployed | Ensure the `name` attribute matches the lookup string and that `context.xml` is under `META-INF/` |
| `Communications link failure` | MySQL not running or wrong host/port | Start MySQL and verify the JDBC URL |
| `Unknown database 'gestion_ecole'` | Database not created | Run the SQL in [step 2](#2-create-the-database) |
| `ClassNotFoundException: jakarta.servlet.*` | Running on Tomcat 9 or older | Use Tomcat 10+ (Jakarta EE 10) |
| `Access denied for user 'root'@'localhost'` | Wrong credentials | Update `username`/`password` in `context.xml` |

---

## Possible Improvements

Ideas for extending this workshop project:

- [ ] Add Bean Validation (`@NotNull`, `@Email`) on the `Apprenant` model
- [ ] Return proper `Location` headers on `POST` responses
- [ ] Add pagination and filtering to `GET /api/apprenants`
- [ ] Introduce a service layer between controllers and DAOs
- [ ] Replace manual JDBC with JPA/Hibernate
- [ ] Add unit and integration tests (JUnit 5, Testcontainers)
- [ ] Add API documentation with OpenAPI / Swagger UI
- [ ] Dockerize the application and database with `docker-compose`
- [ ] Add authentication and authorization (JWT or Jakarta Security)

---

## License

This project is provided for **educational use** and is not currently licensed for commercial redistribution unless otherwise specified by the project owner.

---

## Author

**Mouad**

---

<p align="center">
  <sub>Built as part of a Java EE / Jakarta EE </sub>
</p>