# 🛍️ Products Microservice

## 📖 Overview
The **Products Microservice** is a core component of a modular **e-commerce platform**, responsible for managing **product data, categories, tags, SKUs, images, and related business logic**.  
It exposes **RESTful APIs** for CRUD operations and advanced queries, supporting seamless integration with other microservices and external systems.

---

## 💼 Business Context
This service enables:

- 🛒 **Product catalog management** (add, update, delete, search)
- 🏷️ **Category and tag assignment**
- 📦 **SKU and benefit management**
- 🖼️ **Image handling** for products and parent items
- ⚙️ **Extensible backing services** for custom business rules

---

## ⚙️ Prerequisites
Before running the service, ensure you have the following installed:

- **Java 21+**
- **Maven 3.8+**
- **MySQL 8+** (or compatible database)
- **Docker** (for containerization)
- **Docker Compose** (for multi-service orchestration)
- **ELK Stack** (Elasticsearch, Logstash, Kibana) – for logging and monitoring
- **SonarQube** *(optional)* – for code quality and static analysis

---

## 🧰 Tools & Frameworks Used

| Tool | Description |
|------|--------------|
| **Spring Boot** | Main framework for REST APIs, dependency injection, and configuration |
| **Maven** | Build automation and dependency management |
| **Lombok** | Reduces boilerplate code for models and DTOs |
| **Log4j2** | Advanced logging, configured via `log4j2.xml` |
| **Swagger / OpenAPI** | API documentation and interactive testing |
| **MySQL Connector/J** | JDBC driver for database connectivity |
| **Mapper Mini-Framework (Dozer)** | Simplifies entity-to-DTO mapping |
| **ELK Stack (Elasticsearch, Logstash, Kibana)** | Centralized logging and visualization |
| **Docker / Docker Compose** | Containerization and orchestration |
| **SonarQube** | Static code quality and coverage analysis |

---

## ⚙️ Configuration

**Key configuration files:**

| File                                   | Description                                 |
|----------------------------------------|---------------------------------------------|
| `application.properties` / `application-dev.properties` | Environment-specific settings (DB, logging, etc.) |
| `log4j2.xml`                           | Logging configuration                       |
| `pom.xml`                              | Build and dependency configuration          |

---

## 🚀 How to Run

### 🧑‍💻 Local Development

1. **Clone the repository**
   ```bash
   git clone https://github.com/<your-username>/products-microservice.git
   cd products-microservice
   ```
2. **Configure your database** in `application.properties`
3. **Build the project**
   ```bash
   mvn clean install
   ```
4. **Run the microservice**
   ```bash
   mvn spring-boot:run
   ```

Access Swagger UI at:
👉 [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

---

## 📦 ELK Integration

- Ensure **Logstash** and **Elasticsearch** are running.
- Configure **Log4j2** to send logs to Logstash (see `log4j2.xml`).
- Use **Kibana** to visualize logs and application metrics.

---

## 🧩 Mapper Mini-Framework (Custom)

This project provided a **custom Mapper utility** for object mapping between entities and DTOs. Key features:

- **No configuration required:** Works out-of-the-box, no XML or annotations needed.
- **Automatic caching:** Uses internal caches for constructors, fields, and enums for high performance.
- **Callback support:** Allows you to provide custom mapping logic via callback functions.
- **Supports collections and maps:** Handles nested collections and maps automatically.
- **Type-safe and extensible:** Works with primitives, enums, complex types, and supports custom extensions.

**Example usage:**
```java
// Simple mapping
ProductDto dto = transform(productEntity, ProductDto.class);

// Custom mapping with callback
ProductDto dto = Utilities.transform(productEntity, ProductDto.class, (src, dest) -> {
    // Custom logic here
    dest.setCustomField(src.getSomeValue() + "_custom");
    return dest;
});
```

**Advantages over traditional mappers:**
- No external dependencies or configuration files
- Fast, thanks to caching
- Flexible for advanced mapping scenarios

For more details, see `src/main/java/com/apogee/product/utilities/Mapper.java` and `Utilities.java`.

---

## 📘 API Documentation

The service uses **Swagger** annotations for API documentation.
Access the interactive documentation at:
👉 [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

---

# 🔍 SonarQube Integration

Static analysis can be run via **Maven** with **SonarQube** configured in your `pom.xml`.

### 🧪 Example Command
```bash
mvn clean verify sonar:sonar \
  -Dsonar.projectKey=products-microservice \
  -Dsonar.host.url=http://localhost:9000 \
  -Dsonar.login=<your-token>
```

---

## 🤝 Contribution

1. **Fork** this repository.
2. **Create a new branch** for your feature or fix:
   ```bash
   git checkout -b feature/your-feature
   ```
3. **Commit** your changes.
4. **Submit a Pull Request**.

**Please ensure:**
- Code style follows existing conventions.
- Tests are added for new features.

---

## 🆘 Support

For issues or feature requests:
- Open a **GitHub Issue**
- Or contact the maintainer listed in `pom.xml`

---

## 👨‍💻 Maintainer

**Mohammed Hassan**  
📧 mohammedhassan101994@gmail.com
📧 mohammed_hassan2525@yahoo.com

🔗 [LinkedIn](https://www.linkedin.com/in/mhbughdadi/) | [GitHub](https://github.com/mhbughdadi)

---

> "Built with passion for scalable, modular, and maintainable microservice architecture."
