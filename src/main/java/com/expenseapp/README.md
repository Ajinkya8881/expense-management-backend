# Expense Management Backend

A RESTful backend application for managing personal expenses, monthly budgets, and financial summaries.

The system provides secure authentication using JWT and supports expense tracking, category management, monthly budgeting, and dashboard analytics.

---

## Tech Stack

- Java 17
- Spring Boot 3.5
- Spring Security (JWT, Stateless)
- Spring Data JPA
- MySQL
- Maven

---

## Features

- User registration & login (JWT-based authentication)
- Role-based authorization
- Category management (user-specific)
- Expense CRUD operations
- Date-based expense filtering
- Monthly expense summary with category breakdown
- Monthly budget management
- Financial dashboard (budget vs spending overview)
- Global exception handling & validation

---

## Project Structure

com.expenseapp
├── user
├── category
├── expense
├── budget
├── dashboard
├── security
├── common


### Layered Architecture

- Controller → Handles HTTP
- Service → Business logic
- Repository → Database interaction
- DTO → Request/Response mapping
- Entity → Database models

---

## Database Configuration

Create database:

expense_db


application.properties:

spring.datasource.url=jdbc:mysql://localhost:3306/expense_db
spring.datasource.username=root
spring.datasource.password=YOUR_PASSWORD

spring.jpa.hibernate.ddl-auto=update
spring.jpa.open-in-view=false


---

## JWT Configuration

jwt.secret=your_secret_key
jwt.expiration=86400000


Token validity: 24 hours

---

## Running the Application

mvn clean install
mvn spring-boot:run


Server runs on:

http://localhost:8080


---

# API Endpoints

All secured endpoints require:

Authorization: Bearer <JWT_TOKEN>


---

## 1. Authentication

### Register

POST `/auth/register`

```json
{
  "name": "Ajinkya",
  "email": "ajinkya@test.com",
  "password": "123456",
  "role": "ROLE_USER"
}
Login
POST /auth/login

{
  "email": "ajinkya@test.com",
  "password": "123456"
}
2. Category
Create Category
POST /categories

{
  "name": "Food"
}
Get All Categories
GET /categories

3. Expense
Create Expense
POST /expenses

{
  "title": "Groceries",
  "amount": 1200.50,
  "expenseDate": "2025-01-05",
  "categoryId": 1
}
Get Expenses
GET /expenses?page=0&size=10&sort=amount,desc

GET /expenses?startDate=2025-01-01&endDate=2025-01-31

Update Expense
PUT /expenses/{id}

Delete Expense
DELETE /expenses/{id}

4. Monthly Summary
GET /expenses/summary?year=2025&month=1

5. Budget
Create or Update Budget
POST /budgets

{
  "year": 2025,
  "month": 1,
  "amount": 15000
}
Get Budget
GET /budgets?year=2025&month=1

6. Dashboard
GET /dashboard?year=2025&month=1

Example Test Flow
Register user

Login and copy JWT

Create category

Create expenses

Set monthly budget

Fetch monthly summary

Fetch dashboard