# Bank Appointment Booking System

A three-tier Bank Services Appointment Booking System built with React, Spring Boot, and MySQL for learning DevOps concepts.

## Project Overview

This project includes:
- Frontend: React + Vite
- Backend: Spring Boot
- Database: MySQL

## Architecture

Frontend (React) -> Backend (Spring Boot API) -> MySQL Database

## Prerequisites

Before running the project, make sure you have:

- Java 11+
- Maven 3.6+
- Node.js 16+
- npm 8+
- MySQL 8.0+

## 1) Setup MySQL Database

### Start MySQL

On macOS:
```bash
brew services start mysql
```

On Linux:
```bash
sudo systemctl start mysql
```

On Windows:
```bash
net start MySQL80
```

### Create Database and User

```bash
mysql -u root -p
```

Run:

```sql
CREATE DATABASE bank_appointments;
CREATE USER 'bank_user'@'localhost' IDENTIFIED BY 'bank_password_123';
GRANT ALL PRIVILEGES ON bank_appointments.* TO 'bank_user'@'localhost';
FLUSH PRIVILEGES;
EXIT;
```

### Load Database Schema

From the project root:

```bash
mysql -u bank_user -p bank_appointments < database/schema.sql
```

If you want sample data:

```bash
mysql -u bank_user -p bank_appointments < database/sample-data.sql
```

## 2) Run the Backend

Open a terminal and go to the backend folder:

```bash
cd backend
```

If the application properties are not configured yet, update:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/bank_appointments
spring.datasource.username=bank_user
spring.datasource.password=bank_password_123
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.jpa.database-platform=org.hibernate.dialect.MySQL8Dialect
spring.jpa.hibernate.ddl-auto=validate
server.port=8080
```

Then run:

```bash
mvn clean install
mvn spring-boot:run
```

The backend should start on:

```text
http://localhost:8080
```

## 3) Run the Frontend

Open a second terminal and go to the frontend folder:

```bash
cd frontend
npm install
npm run dev
```

The frontend should run on:

```text
http://localhost:5173
```

## 4) Access the Application

- Frontend: http://localhost:5173
- Backend API: http://localhost:8080

## 5) Common Environment Setup

### Backend config file
`backend/src/main/resources/application.properties`

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/bank_appointments
spring.datasource.username=bank_user
spring.datasource.password=bank_password_123
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.jpa.database-platform=org.hibernate.dialect.MySQL8Dialect
spring.jpa.hibernate.ddl-auto=validate
server.port=8080
```

### Frontend config file
`frontend/.env`

```env
VITE_API_URL=http://localhost:8080/api
```

## 6) Quick Startup Sequence

Run in this order:

```bash
# 1. Start MySQL
brew services start mysql

# 2. Start backend
cd backend
mvn spring-boot:run

# 3. Start frontend
cd frontend
npm install
npm run dev
```

## 7) Troubleshooting

### MySQL connection error
Check that MySQL is running and your credentials are correct.

```bash
mysql -u bank_user -p bank_appointments
```

### Backend not starting
Check:
- Java is installed
- Maven dependencies are downloaded
- Database credentials are correct
- MySQL is running

### Frontend not loading
Check:
- `npm install` completed successfully
- Vite dev server started
- correct API URL is in `.env`

## 8) Notes for Developers

This project is intended for local development and learning. The backend and frontend are run separately, and the database is managed by MySQL.

## License

This project is open source.
