# User Information Backend

A Spring Boot REST API with JWT authentication, role-based access control, device management, and audit logging.

## 🚀 Live Demo
https://user-information-backend-production.up.railway.app

## 🛠️ Tech Stack

- Java 21
- Spring Boot 3
- Spring Security
- JWT Authentication
- PostgreSQL
- JPA / Hibernate
- Railway (Deployment)

## 📋 Prerequisites

- Java 21+
- Maven
- PostgreSQL

## ⚙️ Local Setup

### 1. Clone the repository
```bash
git clone https://github.com/prashant1785/user-information-backend.git
cd user-information-backend
```

### 2. Create PostgreSQL database
```sql
CREATE DATABASE user_information_db;
```

### 3. Configure `application.yaml`
The app uses environment variables with local fallbacks — no changes needed for local development. Default local config:
URL:      jdbc:postgresql://localhost:5432/user_information_db
Username: postgres
Password: postgres
Port:     8080

### 4. Run the application
```bash
./mvnw spring-boot:run
```
Backend runs at:
http://localhost:8080

## 🔐 Authentication

| Endpoint | Method | Access | Description |
|---|---|---|---|
| /auth/register | POST | Public | Register new user |
| /auth/login | POST | Public | Login and get tokens |
| /auth/refresh | POST | Public | Refresh access token |

## 👥 Users

| Endpoint | Method | Access | Description |
|---|---|---|---|
| /users | GET | Authenticated | Get logged in user |
| /users/all | GET | Authenticated | Get all users |

## 🔑 Super Admin

| Endpoint | Method | Access | Description |
|---|---|---|---|
| /super-admin/users/{id}/role | PUT | SUPER_ADMIN, DEVELOPER | Update user role |
| /super-admin/users/delete/{id} | DELETE | SUPER_ADMIN, DEVELOPER | Delete user |
| /super-admin/users/create | POST | SUPER_ADMIN, DEVELOPER | Create user with role |

## 📱 Devices

| Endpoint | Method | Access | Description |
|---|---|---|---|
| /devices/my | GET | All roles | Get my devices |
| /devices/all | GET | SUPER_ADMIN, DEVELOPER | Get all devices |
| /devices/add | POST | All roles | Add new device |
| /devices/delete/{id} | DELETE | SUPER_ADMIN, DEVELOPER | Delete device |
| /devices/delete-by-user-id/{id} | DELETE | SUPER_ADMIN, DEVELOPER | Delete devices by user |

## 📋 Audit Logs

| Endpoint | Method | Access | Description |
|---|---|---|---|
| /audit/logs | POST | SUPER_ADMIN, DEVELOPER | Get paginated audit logs |

### Audit Log Request Body
```json
{
  "pageNo": 1,
  "pageSize": 10,
  "sortColumn": "timestamp",
  "sortWay": "desc",
  "searchKey": "",
  "operationFilter": "INSERT",
  "usernameFilter": "",
  "entityFilter": "User",
  "fromDate": "2025-01-01T00:00:00",
  "toDate": "2026-12-31T23:59:59"
}
```

## 👤 Roles

| Role | Access Level |
|---|---|
| USER | Own devices only |
| ADMIN | Users + all devices |
| SUPER_ADMIN | Full access |
| DEVELOPER | Full access |

## 🌍 Environment Variables (Production)

| Variable | Description |
|---|---|
| SPRING_DATASOURCE_URL | PostgreSQL JDBC URL |
| SPRING_DATASOURCE_USERNAME | Database username |
| SPRING_DATASOURCE_PASSWORD | Database password |
| SPRING_JPA_HIBERNATE_DDL_AUTO | DDL mode (update) |
| JWT_SECRET | Secret key (min 32 chars) |
| JWT_ACCESS_TOKEN_EXPIRATION | Access token expiry (ms) |
| JWT_REFRESH_TOKEN_EXPIRATION | Refresh token expiry (ms) |

## 🗂️ Project Structure
src/
├── config/
│   └── SecurityConfig.java
├── controller/
│   ├── AuthController.java
│   ├── AdminController.java
│   ├── UserController.java
│   ├── DeviceController.java
│   └── AuditController.java
├── dto/
│   ├── AuthResponseTO.java
│   ├── LoginRequestTO.java
│   ├── RegisterRequestTO.java
│   ├── DeviceRequestTO.java
│   ├── DeviceResponseTO.java
│   ├── RoleUpdateRequestTO.java
│   ├── AuditTO.java
│   ├── AuditRequestTO.java
│   ├── AuditResponseTO.java
│   └── AuditWrapperTO.java
├── entity/
│   ├── User.java
│   ├── Device.java
│   ├── AuditLog.java
│   ├── Role.java
│   ├── EventType.java
│   ├── AuditOperation.java
│   └── AuditActionConstant.java
├── repository/
│   ├── UserRepository.java
│   ├── DeviceRepository.java
│   └── AuditRepository.java
└── service/
├── UserService.java
├── UserServiceImpl.java
├── DeviceService.java
├── DeviceServiceImpl.java
├── AuditService.java
├── JwtServiceImpl.java
├── JwtAuthenticationFilter.java
└── CustomUserDetailsServiceImpl.java

## 🚀 Deployment
Deployed on **Railway** with PostgreSQL.
Auto-deploys on every push to `main` branch.
