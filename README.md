<div align="center">

# 🏭 NexusStock

### Enterprise Inventory Management System

Built with **Spring Boot 3** · **Java 17** · **Thymeleaf** · **Bootstrap 5** · **Chart.js**

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.5-6DB33F?logo=springboot&logoColor=white)](https://spring.io/projects/spring-boot)
[![Java](https://img.shields.io/badge/Java-17-ED8B00?logo=openjdk&logoColor=white)](https://openjdk.org/)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)

</div>

---

## ✨ Features

| Feature | Description |
|---------|-------------|
| **📊 Real-time Dashboard** | Live metrics, charts, and KPI cards powered by Chart.js |
| **📦 Product Management** | Full CRUD with SKU tracking and stock levels |
| **🔄 Transactional Updates** | Atomic stock-in/stock-out with automatic audit logging |
| **⚠️ Low Stock Alerts** | Configurable minimum thresholds with instant visibility |
| **📋 Audit Trail** | Complete inventory change history with timestamps |
| **🔒 Security Ready** | Spring Security with JWT placeholder (permitAll for dev) |
| **🐳 Docker Ready** | Multi-stage Dockerfile for production deployment |

## 🏗️ Architecture

```
com.nexusstock/
├── config/          # Security, data initialization
├── controller/      # REST API + Thymeleaf controllers
├── dto/             # Request/Response objects
├── entity/          # JPA entities (Product, InventoryLog)
├── repository/      # Spring Data JPA repositories
└── service/         # Business logic with @Transactional
```

## 🚀 Quick Start

### Prerequisites
- Java 17+
- Maven 3.8+

### Run Locally (H2 Database)
```bash
mvn spring-boot:run
```
Open [http://localhost:8080](http://localhost:8080) — Dashboard loads with demo data.

### H2 Console
Navigate to [http://localhost:8080/h2-console](http://localhost:8080/h2-console)
- JDBC URL: `jdbc:h2:mem:nexusstock`
- Username: `sa` / Password: *(empty)*

## 🐳 Docker

### Build & Run
```bash
docker build -t nexusstock .
docker run -p 8080:8080 nexusstock
```

### Production (MySQL)
```bash
docker run -p 8080:8080 \
  -e SPRING_PROFILES_ACTIVE=prod \
  -e DB_HOST=mysql-host \
  -e DB_PORT=3306 \
  -e DB_NAME=nexusstock \
  -e DB_USERNAME=app_user \
  -e DB_PASSWORD=secure_password \
  nexusstock
```

## 📡 REST API

| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/api/products` | List all products |
| `GET` | `/api/products/{id}` | Get product by ID |
| `POST` | `/api/products` | Create product |
| `PUT` | `/api/products/{id}` | Update product |
| `PATCH` | `/api/products/{id}/stock` | Update stock level |
| `DELETE` | `/api/products/{id}` | Delete product |
| `GET` | `/api/products/low-stock` | List low-stock products |
| `GET` | `/api/products/stats` | Dashboard statistics |
| `GET` | `/api/products/activity` | Recent activity log |

### Example: Create Product
```bash
curl -X POST http://localhost:8080/api/products \
  -H "Content-Type: application/json" \
  -d '{"name":"Widget Pro","sku":"WDG-PRO","currentStock":100,"minStockLevel":20}'
```

### Example: Stock Update
```bash
curl -X PATCH http://localhost:8080/api/products/1/stock \
  -H "Content-Type: application/json" \
  -d '{"changeAmount":25,"actionType":"STOCK_IN"}'
```

## ⚙️ Configuration

### Environment Variables (Production)

| Variable | Default | Description |
|----------|---------|-------------|
| `DB_HOST` | `localhost` | MySQL hostname |
| `DB_PORT` | `3306` | MySQL port |
| `DB_NAME` | `nexusstock` | Database name |
| `DB_USERNAME` | `nexusstock_user` | DB username |
| `DB_PASSWORD` | `changeme` | DB password |
| `DB_POOL_SIZE` | `20` | Connection pool size |
| `SERVER_PORT` | `8080` | Application port |

## 📄 License

MIT License — see [LICENSE](LICENSE) for details.
