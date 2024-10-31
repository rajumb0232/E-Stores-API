# E-Store e-commerce application

## Overview

This project is a backend REST API for an e-commerce platform. It enables sellers to create and manage their own online stores, allowing customers to browse and place orders directly from the stores they prefer. The platform is designed with a focus on empowering individual sellers, offering them tools to manage products, orders, and customer interactions efficiently.

## Table of Contents

- [Technologies](#technologies)
- [Features](#key-features)
- [Requirements](#requirements)
- [Installation](#installation)
- [Running the Project](#running-the-project)
- [API Endpoints](#api-endpoints)
- [Testing](#testing)
- [License](#license)

## Technologies

- Java 21
- Spring Boot 3.3.3
- Maven 3.8.x
- PostgreSQL & MongoDB
- Spring Test 
- Spring Security & JWT

## Key Features:
- **Seller Store Management:** Sellers can easily set up and customize their own online storefronts.
- **Product Catalog**: Each seller can create and manage their own product listings.
- **Customer Orders**: Customers can browse individual stores, place orders, and track order statuses.
- **Order Management**: Sellers can process and manage orders, including order fulfillment and tracking.
- **Authentication & Authorization**: Secure authentication for both sellers and customers, with role-based access controls.

## Disclaimer:  
This open-source project is for learning and development purposes only. It is not intended for production use or official release.

## Requirements

To run this project, you'll need:

- Java 21 (or any above version)
- Maven 3.8.x or above installed
- PostgreSQL (any Latest version)
- MongoDB (any latest version)

## Installation

1. **Clone the repository:**
   ```bash
   git clone https://github.com/rajumb0232/E-Stores-API
   cd E-Stores-API
   ```

2. **Set up environment variables:**
   Create an `.env` file in the root directory to specify database URL, credentials, etc. You can also use this example file as reference -> [.env.example](https://github.com/rajumb0232/E-Stores-API/blob/master/E-Stores-API/.env.example)
   
   - `MAIL_USERNAME = your-mail-id`
     - Use any of your Gmail Id that you have used for app password generation.
   
   - `MAIL_PASSWORD = your-app-password`
     - Use the app password generated in you Google Account.
     - [ you can generate app password here https://myaccount.google.com/]
   
   - `POSTGRES_URL = your-PostgreSQL-URL`
     - Provide the PostgreSQL database server url
   
   - `POSTGRES_USERNAME = your-PostgreSQL-username`
     - Provide the PostgreSQL database server username
   
   - `POSTGRES_PASSWORD = your-PostgreSQL-password`
     - Provide the PostgreSQL database server password
   
   - `JWT_SECRET = base-64-encoded-secret`
     - create a base 64 encode secret and provide here as environmental variable
     
     You can create one using the command
     ``` bash
     openssl rand -base64 32
     ```
---
### Note:
You can open the project in your favorite IDE (IntelliJIDEA/Eclipse/Spring-Tool-Suite) **or** run the project in CLI itself.
You can use the following instructions to run on CLI directly. Make sure maven is installed and configured within the system.

--- 

3. **Install dependencies:**

   ```bash
   mvn clean install
   ```

4. **Run the application:**
   ```bash
   mvn spring-boot:run
   ```

## Running the Project

Once the application is up, it will be running at `http://localhost:7000/api/fkv1` by default.

You can customize the port by modifying `application.yml`:
```yml
server:
  port: 7000
```

## API Endpoints

base-URL: `/api/fkv1/`

Swagger UI: `http://localhost:7000/swagger-ui.html`

### Authentication
| Method | Endpoint              | Description                                    |
|--------|-----------------------|------------------------------------------------|
| POST   | `/sellers/register`   | Register a new seller                          |
| POST   | `/customers/register` | Register a new customer                        |
| POST   | `/verify-email`       | OTP verificatin after registration             |
| POST   | `/login`              | Login to obtain access token and refresh token |
| POST   | `/logout`             | Logout from application                        |
| POST   | `/refresh`            | Issues new access token                        |
| POST   | `/revoke-other`       | Blocks all tokens excluding the current one    |
| POST   | `/revoke-all`         | Blocks all tokens issued to user               |

**Find the detailed Documentation through Swagger:** `http://localhost:7000/swagger-ui.html`
(or)
**You can also find it in Postman:** [Postman API](https://e-stores.postman.co/workspace/Team-Workspace~f285e463-d634-46c0-882f-a50b8e8e59f3/api/48bfcf95-c648-4465-92da-6b87a9480b3e?action=share&creator=36942562&active-environment=36942562-c940278d-34a3-4f8f-9b14-8a7141964326)

### Error Handling

The API returns standard HTTP status codes to indicate success or failure:
- 200 OK: The request was successful
- 400 Bad Request: The request was invalid
- 401 Unauthorized: Authentication failed
- 404 Not Found: Resource not found
- 500 Internal Server Error: Something went wrong on the server

--- 

## Testing

You can also use Postman or any API testing tool to manually test the endpoints.

---

## License

<!-- This project is licensed under the [MIT License](LICENSE). -->
