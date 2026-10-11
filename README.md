# 📈 StockVision --- Stock Market Analysis Platform

> A full-stack web application for stock search, market-data analysis,
> and portfolio management.

![Java](https://img.shields.io/badge/Java-17%2B-orange) ![Spring
Boot](https://img.shields.io/badge/Spring%20Boot-4.x-brightgreen)
![React](https://img.shields.io/badge/React-18%2B-61DAFB)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-Database-336791)

## 📌 Overview

**StockVision** is a full-stack Stock Market Analysis and Portfolio
Management application developed using **Java, Spring Boot, React.js,
PostgreSQL, and Twelve Data API**.

The application provides a centralized interface for searching stocks,
viewing market information, retrieving historical price data, and
managing portfolio holdings through buy and sell operations.

The project follows a layered backend architecture with clear separation
between controllers, services, data-access components, repositories, the
database, and external market-data services.

## 🎯 Objectives

-   Provide a centralized platform for stock-market analysis.
-   Search stocks by symbol or query.
-   Retrieve current stock quote information.
-   Retrieve historical stock price information.
-   Manage portfolio holdings.
-   Implement stock buy and sell operations.
-   Store application data using PostgreSQL.
-   Provide REST APIs for frontend-backend communication.
-   Integrate Twelve Data for market information.
-   Implement authentication and authorization using Spring Security.

## ✨ Features

### 🔍 Stock Search

Users can search for stocks using a stock symbol or search query.

``` text
User → React → Spring Boot → Twelve Data → Results → React
```

### 📊 Real-Time Stock Quote

The application retrieves available quote information such as:

-   Current price
-   Open price
-   High price
-   Low price
-   Previous close
-   Price change
-   Percentage change

### 📈 Historical Stock Data

Users can retrieve historical stock price information for a selected
stock. The request supports an `outputSize` parameter.

### 💼 Portfolio Management

Users can manage holdings containing information such as:

-   Stock symbol
-   Exchange
-   Quantity
-   Average purchase price
-   Current price
-   Current holding value

### 🟢 Buy Stock

``` http
POST /portfolio/buy
```

``` json
{
  "symbol": "AAPL",
  "exchange": "NASDAQ",
  "quantity": 10
}
```

### 🔴 Sell Stock

``` http
POST /portfolio/sell
```

``` json
{
  "symbol": "AAPL",
  "exchange": "NASDAQ",
  "quantity": 5
}
```

The backend validates the available holding quantity before completing a
sell operation.

### 🔐 Authentication & Authorization

Spring Security is used for securing application resources, with support
for authentication, authorization, JWT-based security, and role-based
access control.

## 🏗️ System Architecture

``` text
┌──────────────────────────────┐
│       React Frontend         │
│          + Vite              │
└──────────────┬───────────────┘
               │ REST API
               ▼
┌──────────────────────────────┐
│      Spring Boot Backend     │
└──────────────┬───────────────┘
               │
       ┌───────┼────────┐
       ▼       ▼        ▼
 Controller  Security  Exception
       │
       ▼
    Service
       │
       ▼
      DAO
       │
       ▼
 Repository
       │
       ▼
 PostgreSQL

Spring Boot Backend
       │
       ▼
 Twelve Data API
```

## 🧩 Backend Architecture

``` text
Controller
    ↓
Service
    ↓
DAO
    ↓
Repository
    ↓
PostgreSQL
```

### Controller Layer

-   Exposes REST endpoints.
-   Receives HTTP requests.
-   Delegates processing to services.
-   Returns API responses.

### Service Layer

Contains business logic including:

-   Input validation
-   Portfolio operations
-   Buy and sell processing
-   Business rules
-   External API response processing

### DAO Layer

Provides the data-access abstraction between services and repositories
or external data sources.

### Repository Layer

Uses Spring Data JPA for:

-   Saving entities
-   Retrieving records
-   Updating records
-   Deleting records
-   Database queries

### DTO Layer

Contains request and response objects used for structured API
communication.

### Entity Layer

Contains JPA entities mapped to database tables.

### Security Layer

Contains authentication and authorization functionality.

### Exception Layer

Contains application-specific exception handling and error responses.

## 📂 Backend Structure

``` text
src/main/java/com/stockmarket
│
├── analysis
│   └── config
├── controller
├── dao
├── dto
├── entity
├── enums
├── exception
├── repository
├── security
└── service
```

  Package             Responsibility
  ------------------- ----------------------------------
  `controller`        REST API endpoints
  `service`           Business logic
  `dao`               Data-access operations
  `repository`        JPA/PostgreSQL operations
  `entity`            Database entities
  `dto`               Request and response objects
  `enums`             Application enumerations
  `exception`         Exception handling
  `security`          Authentication and authorization
  `analysis.config`   Application configuration

## 🎨 Frontend Architecture

The frontend is developed using **React.js and Vite**.

``` text
React Component
      ↓
Frontend Service
      ↓
HTTP Request
      ↓
Spring Boot REST API
      ↓
Backend Processing
      ↓
JSON Response
      ↓
React Component
```

## 📂 Frontend Structure

``` text
frontend
│
├── src
│   ├── components
│   ├── pages
│   ├── services
│   ├── assets
│   ├── App.jsx
│   └── main.jsx
├── public
├── package.json
├── vite.config.js
└── index.html
```

## 🌐 REST API Endpoints

### Stock APIs

  ------------------------------------------------------------------------------------------------------------------------
  Method                  Endpoint                                                                 Purpose
  ----------------------- ------------------------------------------------------------------------ -----------------------
  GET                     `/stocks/search?query={query}`                                           Search stocks

  GET                     `/stocks/{symbol}?exchange={exchange}`                                   Get stock quote

  GET                     `/stocks/{symbol}/history?outputSize={outputSize}&exchange={exchange}`   Get historical data
  ------------------------------------------------------------------------------------------------------------------------

### Portfolio APIs

  Method   Endpoint            Purpose
  -------- ------------------- ---------------
  POST     `/portfolio/buy`    Buy/add stock
  POST     `/portfolio/sell`   Sell stock

## 🗄️ Database

StockVision uses **PostgreSQL** with **Spring Data JPA** and
**Hibernate**.

``` text
Java Entity
    ↓
Spring Data JPA
    ↓
Hibernate
    ↓
JDBC
    ↓
PostgreSQL
```

The database stores persistent application information such as users,
portfolio holdings, transactions, and other required application data.

## 🌍 Twelve Data Integration

Twelve Data is used to retrieve market information.

``` text
React Frontend
      ↓
Spring Boot Backend
      ↓
Twelve Data API
      ↓
Market Data
      ↓
Spring Boot
      ↓
React Frontend
```

Keeping the external API integration in the backend prevents the
frontend from directly handling the API configuration.

## 🛠️ Technology Stack

### Backend

  Technology        Purpose
  ----------------- ------------------------------
  Java              Backend programming language
  Spring Boot       Backend framework
  Spring MVC        REST API development
  Spring Data JPA   Database operations
  Hibernate         ORM
  Spring Security   Application security
  Maven             Dependency management

### Frontend

  Technology   Purpose
  ------------ ---------------------
  React.js     User interface
  Vite         Frontend build tool
  JavaScript   Application logic
  HTML5        Page structure
  CSS3         Styling

### Database & API

  Technology        Purpose
  ----------------- ---------------------
  PostgreSQL        Relational database
  pgAdmin           Database management
  Twelve Data API   Market data

### Development Tools

  Tool      Purpose
  --------- ----------------------
  Eclipse   Backend development
  VS Code   Frontend development
  Postman   API testing
  Git       Version control
  GitHub    Source-code hosting

## ⚙️ Prerequisites

-   Java 17 or higher
-   Maven
-   Node.js
-   npm
-   PostgreSQL
-   Git
-   Postman (optional)

## 🚀 Installation & Setup

### 1. Clone the Repository

``` bash
git clone https://github.com/aasthap169-boop/Stock-Market-Analysis.git
cd Stock-Market-Analysis
```

### 2. Configure PostgreSQL

Create the database:

``` sql
CREATE DATABASE stock_market_db;
```

Configure the local Spring Boot configuration in:

``` text
src/main/resources/application.properties
```

Example:

``` properties
spring.datasource.url=jdbc:postgresql://localhost:5432/stock_market_db
spring.datasource.username=postgres
spring.datasource.password=YOUR_PASSWORD
spring.jpa.hibernate.ddl-auto=update
```

### 3. Configure Twelve Data

Add your own Twelve Data API key to the local configuration.

``` properties
twelvedata.api.key=YOUR_API_KEY
```

Do not commit real credentials.

### 4. Run the Backend

``` bash
mvn spring-boot:run
```

You can also run the Spring Boot application from Eclipse.

### 5. Run the Frontend

``` bash
cd frontend
npm install
npm run dev
```

Open the local URL displayed by Vite.

## 🔐 Security & Configuration

Never commit sensitive information such as:

-   Database passwords
-   Twelve Data API keys
-   JWT secrets
-   Private credentials

The local configuration file is excluded through `.gitignore`:

``` gitignore
src/main/resources/application.properties
```

For production, environment variables or secure configuration management
should be used.

## 🧪 API Testing

Postman can be used to test the backend APIs.

Recommended workflow:

``` text
1. Start PostgreSQL
2. Start Spring Boot
3. Open Postman
4. Send API request
5. Verify HTTP response
6. Verify database changes where applicable
```

Example:

``` http
GET /stocks/AAPL?exchange=NASDAQ
```

## 🔄 Typical User Workflow

``` text
1. Open StockVision
        ↓
2. Search for a stock
        ↓
3. View current stock information
        ↓
4. View historical price information
        ↓
5. Buy/add stock
        ↓
6. View portfolio holding
        ↓
7. Monitor current value
        ↓
8. Sell stock when required
```

## 🧱 Design Principles

The project follows:

-   Separation of concerns
-   Layered architecture
-   RESTful API design
-   DTO-based communication
-   DAO and Repository abstraction
-   Centralized exception handling
-   Secure API access
-   Modular frontend components
-   Separation of frontend and backend responsibilities

## ⚠️ Error Handling

The application is designed to handle:

-   Invalid stock symbols
-   Invalid quantities
-   Insufficient portfolio quantity
-   Invalid exchanges
-   Database errors
-   External API errors
-   Unauthorized access
-   Invalid request data

## 🧪 Testing Areas

### Backend

-   Controller testing
-   Service testing
-   DAO testing
-   Repository testing
-   REST API testing

### Frontend

-   Stock search
-   Stock details
-   Historical data
-   Portfolio operations
-   Buy operation
-   Sell operation
-   API error handling
-   Form validation

### Integration

-   React ↔ Spring Boot
-   Spring Boot ↔ PostgreSQL
-   Spring Boot ↔ Twelve Data API

## 📚 Learning Outcomes

This project demonstrates practical experience with:

-   Core Java
-   Spring Boot
-   REST API development
-   Spring MVC
-   Spring Data JPA
-   Hibernate
-   PostgreSQL
-   React.js
-   Vite
-   JavaScript
-   Spring Security
-   JWT authentication
-   External API integration
-   Axios/API communication
-   Git and GitHub
-   Postman
-   Full-stack application architecture

## 🔮 Future Enhancements

-   Moving averages
-   RSI
-   MACD
-   Bollinger Bands
-   Additional technical indicators
-   Personalized watchlists
-   Price alerts
-   Financial market news
-   Candlestick charts
-   Volume charts
-   Cloud deployment
-   Improved mobile responsiveness

## 📸 Screenshots

Add actual application screenshots to a `screenshots` folder.

Recommended screenshots:

-   Login / Registration
-   Dashboard
-   Stock Search
-   Stock Details
-   Historical Chart
-   Portfolio
-   Buy Stock
-   Sell Stock

Example:

``` markdown
## Dashboard

![Dashboard](screenshots/dashboard.png)

## Stock Details

![Stock Details](screenshots/stock-details.png)

## Portfolio

![Portfolio](screenshots/portfolio.png)
```

## 📁 Complete Repository Structure

``` text
Stock-Market-Analysis/
│
├── src/
│   └── main/
│       ├── java/
│       │   └── com/stockmarket/
│       │       ├── analysis/
│       │       ├── controller/
│       │       ├── dao/
│       │       ├── dto/
│       │       ├── entity/
│       │       ├── enums/
│       │       ├── exception/
│       │       ├── repository/
│       │       ├── security/
│       │       └── service/
│       │
│       └── resources/
│
├── frontend/
│   ├── src/
│   ├── public/
│   ├── package.json
│   └── vite.config.js
│
├── .gitignore
├── pom.xml
└── README.md
```

## 👩‍💻 Project Team

### Aastha Pandey

**Full Stack Developer**

**Technologies:** Java, Spring Boot, Spring Security, REST APIs,
React.js, JavaScript, PostgreSQL, Hibernate, Git and GitHub.

## 📜 License

This project is developed for educational and academic purposes.

## ⭐ Project Summary

**StockVision** combines a React.js frontend, Spring Boot backend,
PostgreSQL database, and Twelve Data market-data integration.

The project demonstrates a complete full-stack workflow:

``` text
User
 ↓
React.js
 ↓
REST API
 ↓
Spring Boot
 ↓
Service
 ↓
DAO
 ↓
Repository
 ↓
PostgreSQL
```

External market-data flow:

``` text
Spring Boot
 ↓
Twelve Data API
 ↓
Market Data
 ↓
StockVision
```

StockVision demonstrates practical implementation of stock search,
market-data retrieval, historical analysis, portfolio management,
authentication, and stock buy/sell operations in a single full-stack
application.
