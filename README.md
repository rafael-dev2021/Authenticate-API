# Authenticate-API [![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)](https://www.java.com/) [![Spring](https://img.shields.io/badge/Spring-6DB33F?style=for-the-badge&logo=spring&logoColor=white)](https://spring.io/) [![MongoDB](https://img.shields.io/badge/MongoDB-4EA94B?style=for-the-badge&logo=mongodb&logoColor=white)](https://www.mongodb.com/) ![JWT](https://img.shields.io/badge/JWT-black?style=for-the-badge&logo=JSON%20web%20tokens)


  
![Project Status](https://img.shields.io/badge/status-under%20development-green) &nbsp; [![GitHub License](https://img.shields.io/github/license/rafael-dev2021/ECommerce?color=blue)](https://github.com/rafael-dev2021/ECommerce/blob/main/LICENSE) &nbsp; [![Java](https://img.shields.io/badge/Java-21.0-orange)](https://www.java.com/) &nbsp; [![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.4-brightgreen)](https://spring.io/projects/spring-boot)






Java blog backend, with Spring security and jwt tokens.

API created using Java, Java Spring, Mongodb as nosql database, Spring Security and JWT for authentication control.

# Installation
1. Clone the repository: 
```bash
git clone https://github.com/rafael-dev2021/Authenticate-API.git
```
2. Install dependencies with Maven:
   -  `Spring Web`,
   -  `Lombok`,
   -  `Spring Boot Dev Tools`,
   -  `Spring Data MongoDB`,
   -  `Spring Security`,
   -  `Hibernate Validator`,
   -  `Java Jwt com.auth0`
     
3. Install [MongoDB](https://www.mongodb.com/)

# Usage
1. Start the application with Maven
2. The API will be accessible at [http://localhost:8080](http://localhost:8080)

# API Endpoints
  ```bash
  -> AccountController

  - POST /v1/auth/login - Login into the App

  - POST /v1/auth/register - Register a new user into the App

  ```
  ```bash
  -> UserController

  - GET - /v1/user/users - Retrieve a list of all users. (only the Admin role authenticated with token)

  - Get - /v1/user/id - Returns 1 (one) user by id. (only the Admin role authenticated with token)

  - Put - /v1/user/id - Update user data. (all authenticated users)

  - DELETE - /v1/user/id - Remove a user by id. (only the Admin role authenticated with token)

  - GET - /v1/user/id - All user posts by user ID. (all authenticated users)

  ```
 ```bash
  -> PostController

  - GET - /v1/post/posts - Retrieve a list of all posts. 

  - Get - /v1/post/id - Returns 1 (one) post by id.

  - POST - /v1/post/create - Create one new post. (all authenticated users)

  - Put - /v1/post/id - Update post. (all authenticated users)

  - DELETE - /v1/post/id - Remove a post by id. (all authenticated users)
  ```
# Authentication
The API uses Spring Security for authentication control. The following roles are available:
```bash
USER -> Standard user role for logged-in users.
ADMIN -> Admin role for managing partners (registering new partners)
```
To access protected endpoints as an ADMIN user, provide the appropriate authentication credentials in the request header.

# Database
The project utilizes `MongoDB` as the database. Use the `@Document` annotation for spring mongodb to create the tables in the database.

# Contributing
When contributing to this project, follow the existing code style, confirm [commit conventions](https://www.conventionalcommits.org/en/v1.0.0/) and send your changes in a separate branch.
