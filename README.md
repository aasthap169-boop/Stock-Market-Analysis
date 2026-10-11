# 📈 StockVision - Stock Market Analysis Platform

StockVision is a full-stack web-based Stock Market Analysis and Portfolio Management application developed using **Java Spring Boot, React.js, PostgreSQL, and Twelve Data API**.

The primary objective of StockVision is to provide users with a centralized platform where they can search for stocks, view real-time market information, analyze historical prices, and manage their stock portfolio through buy and sell operations.

The application follows a layered backend architecture and communicates with external market-data services through REST APIs.

---

## 📌 Project Overview

Stock market data is often distributed across multiple platforms, making it difficult for beginners and individual investors to monitor stocks and their portfolio from a single application.

StockVision addresses this problem by providing:

- Stock search functionality
- Real-time stock quote information
- Historical stock price data
- Portfolio management
- Buy and sell stock operations
- Portfolio holding tracking
- Authentication and authorization
- RESTful backend APIs
- PostgreSQL-based data persistence
- Integration with Twelve Data for market information

The system is designed with a clear separation between the frontend, backend, database, and external market-data provider.

---

# 🎯 Objectives

The major objectives of StockVision are:

1. To develop a centralized stock market analysis platform.
2. To provide users with stock search and market information.
3. To retrieve real-time stock quote data using an external API.
4. To provide historical stock price information for analysis.
5. To allow users to maintain and manage their stock portfolio.
6. To implement stock buying and selling functionality.
7. To securely manage application users.
8. To provide REST APIs for communication between frontend and backend.
9. To persist portfolio and application data using PostgreSQL.
10. To build a scalable layered architecture using Spring Boot.

---

# ✨ Key Features

## 🔍 1. Stock Search

Users can search for stocks using their stock symbol or search query.

The frontend sends the search request to the Spring Boot backend, which communicates with the market-data API and returns the required stock information.

### Example

```text
User enters stock symbol
        ↓
React Frontend
        ↓
Spring Boot REST API
        ↓
Twelve Data API
        ↓
Stock Search Result
        ↓
React UI

📊 2. Real-Time Stock Quote
StockVision retrieves current stock information through the Twelve Data API.
The quote section can provide information such as:
- Current price
- Open price
- High price
- Low price
- Previous close
- Price change
- Percentage change
The backend processes the external API response before returning the required information to the frontend.
📈 3. Historical Stock Data
Users can retrieve historical stock price information for a selected stock.
Historical data can be used to understand the price movement of a stock over a particular period.
The application supports requesting historical data with configurable output size.
Data Flow
Stock Symbol
     ↓
React Frontend
     ↓
Spring Boot Controller
     ↓
Service Layer
     ↓
DAO Layer
     ↓
Twelve Data API
     ↓
Historical Market Data
     ↓
Frontend

💼 4. Portfolio Management
StockVision provides portfolio management functionality that allows users to maintain their stock holdings.
A portfolio can contain information such as:
- Stock symbol
- Exchange
- Quantity
- Average purchase price
- Current price
- Current holding value
The portfolio module is connected with live stock information to provide updated market values.
🟢 5. Buy Stock
Users can purchase stocks through the portfolio module.
The buy operation receives:
Stock Symbol
Exchange
Quantity

The backend validates the request and processes the portfolio transaction.
The holding information is then stored in the PostgreSQL database.
Buy Flow
User selects stock
        ↓
Enters quantity
        ↓
React Frontend
        ↓
POST /portfolio/buy
        ↓
Spring Boot Controller
        ↓
Portfolio Service
        ↓
DAO
        ↓
PostgreSQL

🔴 6. Sell Stock
Users can sell stocks that are available in their portfolio.
The sell operation validates:
- Stock symbol
- Exchange
- Quantity
- Existing portfolio holding
- Available quantity
The system prevents users from selling more shares than they currently hold.
Sell Flow
User selects holding
        ↓
Enters quantity
        ↓
React Frontend
        ↓
POST /portfolio/sell
        ↓
Spring Boot Controller
        ↓
Portfolio Service
        ↓
DAO
        ↓
PostgreSQL

🔐 7. Authentication & Authorization
The application includes security using Spring Security.
Authentication is responsible for verifying the identity of the user, while authorization controls access to protected application resources.
The security layer is designed to protect backend APIs from unauthorized access.
Technologies involved include:
- Spring Security
- JWT-based authentication
- Role-based authorization
🏗️ System Architecture
StockVision follows a layered architecture to keep the application modular and maintainable.
                    ┌─────────────────────┐
                    │    React Frontend   │
                    │       Vite          │
                    └──────────┬──────────┘
                               │
                               │ REST API
                               ↓
                    ┌─────────────────────┐
                    │ Spring Boot Backend │
                    └──────────┬──────────┘
                               │
                 ┌─────────────┼─────────────┐
                 ↓             ↓             ↓
           Controller       Security      Exception
                 │
                 ↓
             Service
                 │
                 ↓
               DAO
                 │
                 ↓
            Repository
                 │
                 ↓
            PostgreSQL

                 Backend
                    │
                    │ External API
                    ↓
             Twelve Data API

🧩 Backend Architecture
The backend follows:
Controller
     ↓
Service
     ↓
DAO
     ↓
Repository
     ↓
Database

Controller Layer
The Controller layer exposes REST endpoints to the frontend.
Responsibilities include:
- Receiving HTTP requests
- Validating request structure
- Calling appropriate service methods
- Returning HTTP responses
Service Layer
The Service layer contains the application's business logic.
Responsibilities include:
- Business validations
- Portfolio calculations
- Buy and sell processing
- Calling DAO methods
- Processing external API responses
- Returning structured responses
DAO Layer
The DAO layer acts as an abstraction between the Service and Repository layers.
Responsibilities include:
- Database operations
- Repository interaction
- External API communication where required
- Data retrieval and persistence
Repository Layer
The Repository layer handles database communication using Spring Data JPA.
It provides functionality for:
- Saving entities
- Finding records
- Updating records
- Deleting records
- Querying portfolio information
📂 Backend Project Structure
src/main/java/com/stockmarket
│
├── analysis
│   └── config
│
├── controller
│
├── dao
│
├── dto
│
├── entity
│
├── enums
│
├── exception
│
├── repository
│
├── security
│
└── service

Package Responsibilities
Package	Responsibility
controller	REST API endpoints
service	Business logic
dao	Data access operations
repository	PostgreSQL/JPA operations
entity	Database entities
dto	Request and response objects
enums	Application enumerations
exception	Custom exception handling
security	Authentication and authorization
analysis.config	Application configuration


🎨 Frontend Architecture
The frontend is developed using:
- React.js
- Vite
- JavaScript
- HTML5
- CSS3
The frontend communicates with Spring Boot through REST APIs.
A service-based approach is used for API communication so that API logic remains separate from UI components.
📂 Frontend Structure
frontend
│
├── src
│   │
│   ├── components
│   │
│   ├── pages
│   │
│   ├── services
│   │
│   ├── assets
│   │
│   ├── App.jsx
│   └── main.jsx
│
├── package.json
├── vite.config.js
└── index.html

🔌 API Communication
The React application communicates with the Spring Boot backend using HTTP requests.
Axios is used for API communication.
Example architecture:
React Component
      ↓
Service File
      ↓
Axios
      ↓
Spring Boot REST API
      ↓
Service Layer
      ↓
DAO
      ↓
Database / External API

🌐 Main REST APIs
Stock APIs
Search Stocks
GET /stocks/search?query={query}

Used to search for stocks based on a search query.
Get Stock Quote
GET /stocks/{symbol}?exchange={exchange}

Used to retrieve current stock quote information.
Get Historical Stock Data
GET /stocks/{symbol}/history?outputSize={outputSize}&exchange={exchange}

Used to retrieve historical price information.
💼 Portfolio APIs
Buy Stock
POST /portfolio/buy

Example request:
{
  "symbol": "AAPL",
  "exchange": "NASDAQ",
  "quantity": 10
}

Sell Stock
POST /portfolio/sell

Example request:
{
  "symbol": "AAPL",
  "exchange": "NASDAQ",
  "quantity": 5
}

🗄️ Database
StockVision uses PostgreSQL as its relational database.
Spring Data JPA and Hibernate are used to communicate with PostgreSQL.
The database is responsible for storing persistent application information such as portfolio holdings, transactions, and user-related data.
🔄 Database Interaction
Application
     ↓
Spring Data JPA
     ↓
Hibernate
     ↓
JDBC
     ↓
PostgreSQL

Hibernate manages object-relational mapping between Java entities and PostgreSQL tables.
🌍 External Market Data API
StockVision integrates with Twelve Data API to retrieve market information.
The external API is used for operations such as:
- Stock search
- Current stock quotes
- Historical stock data
The backend acts as an intermediate layer between the frontend and the external API.
This prevents the frontend from directly handling the external API configuration.
React
  ↓
Spring Boot
  ↓
Twelve Data
  ↓
Market Data

🛠️ Technology Stack
Backend
Technology	Purpose
Java	Backend programming language
Spring Boot	Backend framework
Spring MVC	REST API development
Spring Data JPA	Database operations
Hibernate	ORM
Spring Security	Application security
Maven	Dependency management


Frontend
Technology	Purpose
React.js	User interface
Vite	Frontend build tool
JavaScript	Application logic
HTML5	Page structure
CSS3	Styling


Database
Technology	Purpose
PostgreSQL	Relational database
pgAdmin	Database management


Tools
Tool	Purpose
Eclipse	Backend development
VS Code	Frontend development
Postman	API testing
Git	Version control
GitHub	Source code management


⚙️ Prerequisites
Before running the project, install:
- Java 17+
- Maven
- Node.js
- npm
- PostgreSQL
- Git
- Postman (optional)
🚀 Installation
1. Clone Repository
git clone https://github.com/aasthap169-boop/Stock-Market-Analysis.git

Move into the project directory:
cd Stock-Market-Analysis

🗄️ 2. Configure PostgreSQL
Create a PostgreSQL database.
Example:
CREATE DATABASE stock_market_db;

Update your local database configuration inside:
src/main/resources/application.properties

Example configuration:
spring.datasource.url=jdbc:postgresql://localhost:5432/stock_market_db
spring.datasource.username=postgres
spring.datasource.password=YOUR_PASSWORD

spring.jpa.hibernate.ddl-auto=update

🔑 3. Configure Twelve Data API
Create/configure your Twelve Data API key in your local application configuration.
Example:
twelvedata.api.key=YOUR_API_KEY

Do not upload real API keys or database passwords to GitHub.
▶️ 4. Run Backend
From the backend project root:
mvn spring-boot:run

Or run the main Spring Boot application from Eclipse.
▶️ 5. Run Frontend
Open another terminal and navigate to the frontend directory:
cd frontend

Install dependencies:
npm install

Start the development server:
npm run dev

The Vite development server will provide the local frontend URL.
🔒 Security Configuration
Sensitive information should remain outside version control.
The following type of information must not be committed:
Database password
API keys
JWT secret
Other private credentials

The local configuration file is excluded through .gitignore.
Example:
src/main/resources/application.properties

For deployment, environment variables or a secure configuration mechanism should be used.
🧪 API Testing
The backend APIs can be tested using Postman.
Example workflow:
1. Start PostgreSQL
2. Start Spring Boot Backend
3. Open Postman
4. Send API request
5. Verify response
6. Check database changes

Example:
GET /stocks/AAPL?exchange=NASDAQ

Portfolio operation:
POST /portfolio/buy

Request body:
{
  "symbol": "AAPL",
  "exchange": "NASDAQ",
  "quantity": 10
}

🔄 Application Workflow
A typical StockVision workflow is:
                USER
                  │
                  ↓
          React Web Application
                  │
          ┌───────┴────────┐
          ↓                ↓
     Stock Search      Portfolio
          │                │
          ↓                ↓
    Spring Boot REST APIs
          │
     ┌────┴─────┐
     ↓          ↓
PostgreSQL   Twelve Data
     │          │
     └────┬─────┘
          ↓
     Response Data
          ↓
      React UI

📌 Example Use Case
User Searches for a Stock
1. User opens StockVision.
2. User enters a stock symbol.
3. React sends a request to the backend.
4. Spring Boot receives the request.
5. Backend calls the Twelve Data API.
6. Market information is received.
7. Backend processes the response.
8. Data is returned to React.
9. React displays the stock information.
💰 Portfolio Example
Suppose a user buys:
Stock: AAPL
Exchange: NASDAQ
Quantity: 10

The frontend sends the request to:
POST /portfolio/buy

The backend:
1. Validates the request.
2. Retrieves the required stock information.
3. Processes the portfolio operation.
4. Stores/update the holding.
5. Returns the operation result.
Later, when the user sells 5 shares:
Existing Quantity = 10
Sold Quantity     = 5
Remaining Quantity = 5

The backend validates that the user owns sufficient quantity before completing the operation.
🧱 Design Principles
The project follows several software development principles:
- Separation of concerns
- Layered architecture
- RESTful API design
- DTO-based request/response handling
- Centralized exception handling
- Database abstraction using DAO and Repository layers
- Secure API access
- Modular frontend components
🔮 Future Enhancements
The project can be extended with additional functionality such as:
📊 Advanced Analytics
- Moving averages
- RSI
- MACD
- Bollinger Bands
- Technical indicators
📋 Watchlist
Users can create personalized stock watchlists and monitor selected stocks.
🔔 Price Alerts
Users can configure price-based alerts for selected stocks.
📰 Market News
Integrate a financial news API to display relevant market news.
📈 Advanced Charts
Interactive candlestick and technical-analysis charts can be added.
☁️ Cloud Deployment
The application can be deployed using:
- AWS
- Azure
- Render
- Railway
- Other cloud platforms
📱 Responsive UI
Further improvements can make the application optimized for mobile and tablet devices.
🧪 Testing Strategy
The application can be tested at multiple levels.
Backend Testing
- Controller testing
- Service testing
- DAO testing
- Repository testing
- API testing
API Testing
Postman can be used to test:
GET
POST
PUT
DELETE

requests where applicable.
Frontend Testing
Frontend functionality can be verified through:
- User interaction
- API response handling
- Form validation
- Error handling
- Portfolio updates
⚠️ Error Handling
The backend includes an exception-handling structure to provide meaningful responses when errors occur.
Possible error scenarios include:
- Invalid stock symbol
- Invalid quantity
- Insufficient portfolio quantity
- Invalid exchange
- Database connection failure
- External API failure
- Unauthorized API access
- Invalid request data
📚 Learning Outcomes
Through the development of StockVision, the project demonstrates practical experience with:
- Java programming
- Spring Boot
- REST API development
- Spring Data JPA
- Hibernate
- PostgreSQL
- React.js
- API integration
- Spring Security
- JWT authentication
- Git and GitHub
- Postman API testing
- Layered architecture
- Full-stack application development
👥 Project Team
Aastha Pandey
Full Stack Developer
Technologies:
Java
Spring Boot
React.js
PostgreSQL
Spring Security
REST APIs
JavaScript
Git & GitHub

📸 Project Screenshots
Add application screenshots here after completing the UI.
Example:
## Dashboard

![StockVision Dashboard](screenshots/dashboard.png)

## Stock Search

![Stock Search](screenshots/stock-search.png)

## Portfolio

![Portfolio](screenshots/portfolio.png)

Recommended screenshots:
- Login Page
- Dashboard
- Stock Search
- Stock Details
- Historical Chart
- Portfolio
- Buy Stock
- Sell Stock
📁 Recommended Repository Structure
Stock-Market-Analysis/
│
├── src/
│   └── main/
│       ├── java/
│       │   └── com/stockmarket/
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
├── README.md
└── ...

📜 License
This project is developed for educational and academic purposes.
⭐ Project Summary
StockVision is a full-stack stock market analysis and portfolio management application that combines a React.js frontend, Spring Boot REST backend, PostgreSQL database, and Twelve Data market API.
The project demonstrates the complete flow of a modern full-stack application:
User Interface
      ↓
React.js
      ↓
REST API
      ↓
Spring Boot
      ↓
Service Layer
      ↓
DAO Layer
      ↓
PostgreSQL
