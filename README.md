# 💼 Transaction Management System

![Java](https://img.shields.io/badge/Java-17-blue?logo=java)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.1-green?logo=spring)
![MySQL](https://img.shields.io/badge/MySQL-8.0-orange?logo=mysql)
![Fly.io](https://img.shields.io/badge/Deployed_on-Fly.io-purple?logo=fly.io)
![License](https://img.shields.io/badge/License-MIT-brightgreen)

A high-performance backend system for managing financial transactions, built with modern Java technologies and deployed on cloud infrastructure.

<div align="center">
  <img src="https://i.imgur.com/JDX6w0d.png" alt="System Architecture" width="600"/>
</div>

## ✨ Features

- 🏗️ Clean layered architecture (Controller-Service-Repository)
- 🔄 Environment-specific configurations
- 🛡️ Robust error handling
- 📊 MySQL database integration
- ☁️ Cloud-native deployment
- 📈 Scalable design

## 🛠️ Tech Stack

| Category       | Technology           |
|----------------|----------------------|
| Language       | Java 17             |
| Framework      | Spring Boot 3.1     |
| Database       | MySQL 8.0           |
| Deployment     | Fly.io              |
| DB Hosting     | Clever Cloud        |
| Build Tool     | Maven               |

## 🏗️ Project Structure
transaction-manager/
├── src/
│ ├── main/
│ │ ├── java/com/transaction/
│ │ │ ├── config/ # Configuration classes
│ │ │ ├── controller/ # REST endpoints
│ │ │ ├── dto/ # Data transfer objects
│ │ │ ├── exception/ # Error handling
│ │ │ ├── repository/ # Database operations
│ │ │ ├── service/ # Business logic
│ │ │ └── TransactionManagerApplication.java
│ │ └── resources/
│ │ ├── application.properties
│ │ ├── application-local.properties
│ │ └── application-prod.properties
│ └── test/ # Unit & integration tests
├── .gitignore
├── pom.xml
└── README.md


## 🚀 Quick Start

### Prerequisites
- Java 17 JDK
- MySQL 8.0+
- Maven 3.8+

☁️ Deployment
Fly.io Setup
Install Fly CLI:

bash
curl -L https://fly.io/install.sh | sh
Login and deploy:

bash
flyctl auth login
flyctl launch
flyctl deploy
Set production secrets:

bash
flyctl secrets set SPRING_PROFILES_ACTIVE=prod
flyctl secrets set SPRING_DATASOURCE_URL=$DB_URL
flyctl secrets set SPRING_DATASOURCE_USERNAME=$DB_USER
flyctl secrets set SPRING_DATASOURCE_PASSWORD=$DB_PASS

📈 Best Practices
✔️ Separation of Concerns
✔️ DTO Pattern Implementation
✔️ Global Exception Handling
✔️ Environment-aware Configuration
✔️ CI/CD Ready
✔️ Cloud-native Design

🤝 Contributing
We welcome contributions! Please follow these steps:

Fork the repository

Create your feature branch (git checkout -b feature/AmazingFeature)

Commit your changes (git commit -m 'Add some AmazingFeature')

Push to the branch (git push origin feature/AmazingFeature)

Open a Pull Request

📜 License
Distributed under the MIT License. See LICENSE for more information.

📬 Contact
