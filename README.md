# 📚 Bookstore

---

## Project Overview

**Bookstore** is a comprehensive web application designed to facilitate the online selection and purchase of books across various categories. This platform allows users to browse, add to cart, and purchase books without the need to visit a physical store, offering a seamless and flexible shopping experience.

The application leverages modern Java-based server-side technologies, including the Spring Framework, to ensure robust and scalable performance. For security, JWT (JSON Web Tokens) is used to manage authentication and authorization, providing secure access control throughout the application. The application is containerized using Docker, making it easy to deploy and manage in various environments, including cloud services like AWS.

---
## 🌟 Key Features

- **Category Management**: Organize books into categories for easier browsing.
- **Shopping Cart**: Add books to a shopping cart for streamlined purchasing.
- **User Authentication**: Secure login and registration using JWT.
- **Role-Based Access Control**: Admin and user roles to manage permissions.
- **RESTful APIs**: Clean and efficient APIs for all operations.
- **Dockerized Deployment**: Simplified deployment using Docker and Docker Compose.
- **Database Integration**: MySQL database support with schema management via Liquibase.
---
### 🔑 Authentication Management

Endpoints for managing user authentication:

- **POST**: `/auth/registration` - Register a new user.
- **POST**: `/auth/login` - Log in an existing user.

### 📦 Order Management

Endpoints for managing user orders:

- **GET**: `/orders` - Retrieve user's order history.
- **POST**: `/orders` - Place a new order.
- **PATCH**: `/orders/{id}` - Update the status of an existing order.
- **GET**: `/orders/{orderId}/items` - Retrieve all items for a specific order.
- **GET**: `/orders/{orderId}/items/{itemId}` - Retrieve a specific item within an order.

### 🗂️ Category Management

Endpoints for managing categories:

- **GET**: `/categories` - Find all categories.
- **POST**: `/categories` - Save a new category to the database.
- **GET**: `/categories/{id}` - Find a category by its ID.
- **POST**: `/categories/{id}` - Update category data in the database.
- **DELETE**: `/categories/{id}` - Delete a category by its ID.
- **GET**: `/categories/{id}/books` - Get all books associated with a specific category ID.

### 🛒 Shopping Cart Management

Endpoints for managing the shopping cart:

- **PUT**: `/cart/items/{cartItemId}` - Update the quantity of a book in the shopping cart.
- **DELETE**: `/cart/items/{cartItemId}` - Remove a book from the shopping cart.
- **GET**: `/cart` - Retrieve the user's shopping cart.
- **POST**: `/cart` - Add a book to the shopping cart.

### 📚 Book Management

Endpoints for managing books:

- **GET**: `/books` - Find all books.
- **POST**: `/books` - Save a new book to the database.
- **GET**: `/books/{id}` - Find a book by its ID.
- **POST**: `/books/{id}` - Update book data in the database.
- **DELETE**: `/books/{id}` - Delete a book by its ID.
- **GET**: `/books/search` - Search for books by various parameters.
---
## 🛠️ Technologies Used

- Java 17
- Spring Boot 3.1.0, Spring Security, Spring data JPA
- REST, Mapstruct
- MySQL 8.0, Liquibase
- Maven, Docker
- Lombok, Swagger
- Junit, Mockito
---
## 🚀 Getting Started

### Prerequisites

Before you begin, ensure you have met the following requirements:

- [Docker](https://www.docker.com/get-started) installed on your machine.
- [Docker Compose](https://docs.docker.com/compose/install/) installed.

### Installation

1. **Clone the Repository**:

    ```sh
    git clone https://github.com/yourusername/bookstore.git
    cd bookstore
    ```

2. **Configure Environment Variables**:

   Customize the `.env` file to set up your environment-specific variables such as database credentials, JWT secret keys, etc. This allows you to connect to your own database or adjust configurations as needed.


3. **Set Up the Environment**:

   Ensure Docker and Docker Compose are installed on your system. You can configure environment variables directly in the `docker-compose.yml` file.


4. **Build and Run the Application**:

    ```sh
    docker-compose up --build
    ```

5. **Access the Application**:

   The application will be available at `http://localhost:8080/api`.


6. **API Documentation**:

   Access the API documentation via Swagger at `http://localhost:8080/api/swagger-ui/index.html#/`.

### 📬 Contact

For any inquiries or support, please contact [tarasyashchuk089@gmail.com](mailto:tarasyashchuk089@gmail.com).

---

> This README file was last updated on 2024-08-13.
