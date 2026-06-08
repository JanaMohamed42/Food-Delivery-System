
# Spring Boot - Food Delivery System

> Javeats Lite is  food delivery platform concept that connects customers with restaurants, enabling browsing, ordering, payments, and delivery tracking.  

---

## Notes
- **Feature**: A high-level capability of the system derived from the use case diagram. Each feature represents a core business area of the platform.
- **Functionalities**: Specific actions or operations that can be performed under each feature to achieve a user goal.
- **Resource**: The main data entity or object handled by the system, typically mapped to a database table or API model.
- **API Endpoint**: A specific URL pattern combined with an HTTP method (GET, POST, PUT, DELETE) used to interact with a resource.

---

## Future Work
- Implement backend REST API using JAVA/Spring .
- Create a responsive frontend interface for customers and restaurants.
- Integrate with real-world payment gateways (e.g., Stripe, PayPal).
- Add real-time delivery tracking with WebSockets.
- Implement authentication and authorization with JWT.
---

##  Project Goals

| # | Goal | Description |
|---|------|-------------|
| 1 |  Document system features | Organize and explain all system features in a structured way |
| 2 |  Model using UML diagrams | Create ERD, Flowcharts, Sequence Diagrams, and System Scheme |
| 3 |  Define requirements | Document both functional (what) and non-functional (how well) requirements |
| 4 |  Build a prototype | Develop a working prototype that demonstrates core functionality |

---
## 📑 Table of Contents

README.md – Project overview & main documentation

docs/ – Project documentation

- requirements/ – Features & functionalities of each module
- diagrams/ – Use case, sequence, and class diagrams
- srs/ – System Requirement Specification (optional)

src/ – Source code (future implementation)

- backend/ – Server-side logic & APIs

  ---
  ## Architecture

The project follows a layered architecture with clear separation of concerns:

src/main/java/com/fooddelivery/
├── controller/ ← REST endpoints (HTTP layer) - Receives requests from clients
├── service/ ← Business logic - Handles core application rules
├── repository/ ← JPA interfaces (data layer) - Communicates with database
├── entity/ ← DB entities + Cart (Redis POJO) - Maps to database tables
├── dto/
│ ├── request/ ← Validated request bodies - What client sends
│ └── response/ ← Consistent API output shapes - What client receives
├── mapper/ ← MapStruct mappers - Converts Entity ↔ DTO automatically
├── enums/ ← OrderStatus, RestaurantCategory - Fixed value constants
├── exception/ ← Custom exceptions - Project-specific errors
│ └── handler/ ← GlobalExceptionHandler (@ControllerAdvice) - Catches all errors
└── config/ ← RedisConfig - Configuration classes

### Layer Responsibilities

| Layer | Path | What it does |
|-------|------|--------------|
| Controller | controller/ | Receives HTTP requests, validates input, calls service layer |
| Service | service/ | Contains business logic, handles transactions, coordinates repositories |
| Repository | repository/ | JPA interfaces for database CRUD operations |
| Entity | entity/ | Maps Java objects to database tables |
| DTO Request | dto/request/ | Defines expected request structure with validation |
| DTO Response | dto/response/ | Defines consistent API response format |
| Mapper | mapper/ | Automatically converts Entity ↔ DTO (using MapStruct) |
| Enums | enums/ | Fixed values like OrderStatus (PENDING, CONFIRMED, DELIVERED) |
| Exception | exception/ | Custom exceptions for specific scenarios |
| Handler | exception/handler/ | Global exception handler (@ControllerAdvice) |
| Config | config/ | Configuration classes (Redis, Security, etc.) |

### Tech Stack & Why

| Layer | Technology | Purpose |
|-------|------------|---------|
| API | Spring Boot REST | Exposes HTTP endpoints for clients |
| Mapping | MapStruct | Zero-boilerplate Entity ↔ DTO conversion |
| Cart Storage | Redis | In-memory cart storage with automatic TTL expiry |
| Database | PostgreSQL / MySQL | Persistent storage for orders, users, restaurants |
| ORM | Hibernate + JPA | Manages database operations with ddl-auto=update |
| Connection Pool | HikariCP | Fastest connection pool (pre-configured) |
| Containerization | Docker + Docker Compose | One-command environment setup |
| Security | Non-root user in Docker | Prevents privilege escalation |

### Tech choices at a glance

- MapStruct for zero-boilerplate entity ↔ DTO mapping
- Redis for cart storage with automatic TTL expiry
- Hibernate with ddl-auto=update for schema management
- Multi-stage Dockerfile with non-root user for security
- HikariCP connection pool (pre-configured)

---
## Prerequisites

- Java 17+
- Maven 3.6+
- Docker

---

## Docker Support

### Running with Docker:

```bash
docker-compose up -d

---

#  Food Delivery System – API Design

This project defines the **functional requirements, database entities, and API endpoints** for a food delivery application.  
It covers the entire lifecycle: from **user registration and authentication**, browsing restaurants, managing menus, handling carts & orders, to **payments, offers, and dashboards**.

---


##  Features & Functionalities

| # | Feature                          | Functions                                                                                  | Example API Endpoints |
|---|----------------------------------|--------------------------------------------------------------------------------------------|-----------------------|
| 1 | **User Management & Authentication** | - Sign up / Login / Logout <br> - Forget Password <br> - Email/OTP verification <br> - Social login <br> - Enable/Disable account <br> - Role-based permissions (Admin / Manager / User) <br> - User profile management | - `POST /users` → Register <br> - `POST /sessions/login` → Login <br> - `POST /sessions/logout` → Logout <br> - `PUT /users/{id}` → Edit profile <br> - `DELETE /users/{id}` → Deactivate account |
| 2 | **Restaurant Management**        | - Add / Update / Enable / Disable restaurant <br> - View all restaurants <br> - Search restaurants (filters) <br> - View top-rated restaurants <br> - Restaurant recommendations | - `POST /restaurants` → Add restaurant <br> - `PUT /restaurants/{id}` → Update restaurant <br> - `GET /restaurants` → List restaurants <br> - `GET /restaurants/{id}` → Restaurant details |
| 3 | **Menu Management**              | - Create / Update / Delete menus <br> - View menu list & history <br> - Search menu items <br> - CRUD ingredients | - `POST /restaurants/{id}/menus` → Create menu <br> - `GET /restaurants/{id}/menus` → View menus <br> - `GET /menu-items/{id}` → View item details <br> - `PUT /menu-items/{id}` → Update menu item |
| 4 | **Cart Management**              | - Add item to cart <br> - Modify cart item quantities <br> - View cart <br> - Remove item <br> - Clear cart <br> - Checkout | - `POST /cart-items` → Add item <br> - `PUT /cart-items/{id}` → Update quantity <br> - `GET /cart` → View cart <br> - `DELETE /cart-items/{id}` → Remove item <br> - `DELETE /cart` → Clear cart <br> - `POST /cart/checkout` → Checkout |
| 5 | **Order Management**             | - Place order <br> - Cancel order (customer or restaurant) <br> - Track order status <br> - View order history <br> - Order confirmation (Email/SMS) <br> - Notify customer on updates | - `POST /orders` → Place order <br> - `GET /orders/{id}` → Order details <br> - `GET /orders` → User orders <br> - `PUT /orders/{id}/cancel` → Cancel order <br> - `PUT /orders/{id}/status` → Update status |
| 6 | **Customer Management**          | - Manage multiple addresses <br> - Preferred payment settings <br> - Ratings & comments <br> - Account deactivation <br> - Customer support (chat) | - `GET /users/{id}/orders` → Order history <br> - `POST /users/{id}/address` → Add address <br> - `GET /users/{id}/address` → List addresses <br> - `POST /users/{id}/reviews` → Add review |
| 7 | **Payment Integration**          | - Multiple payment methods (Card, Cash on Delivery, Wallet) <br> - Integration with 3rd-party gateways <br> - Payment validation & verification <br> - Error handling <br> - Payment receipt generation <br> - Transaction auditing | - `POST /payments` → Create payment <br> - `GET /payments/{id}` → Payment details <br> - `GET /payments/transactions` → Transaction history |
| 8 | **Offers & Promotions**          | - View available offers <br> - Apply offer at checkout <br> - Personalized recommendations | - `GET /offers` → List offers <br> - `POST /cart/apply-offer` → Apply coupon |
| 9 | **Dashboard & Reporting**        | - System Dashboard: Count restaurants, active customers, daily/total orders, revenue <br> - Restaurant Dashboard: Daily/total orders, cancellations, revenue per restaurant <br> - Generate daily/monthly transaction reports | - `GET /dashboard/system` → System stats <br> - `GET /dashboard/restaurants/{id}` → Restaurant stats <br> - `GET /reports/daily` → Daily report <br> - `GET /reports/monthly` → Monthly report |

---


##  Database Entities (Detailed Overview)

The database is divided into **modules** for clarity:

###  Users & Roles
- `user` → Basic user details (name, email, password, phone)  
- `role` → Defines system roles (Admin, Manager, Customer)  
- `user_role`, `group`, `group_role`, `user_group` → Manage role-based access and group permissions  

###  Restaurants & Menus
- `restaurant` → Core details (name, address, status)  
- `restaurant_category` → Classifies restaurants by cuisine/type  
- `menu`, `menu_category`, `menu_item`, `ingredient` → Full menu hierarchy  

###  Cart & Orders
- `cart`, `cart_items` → User’s current cart and its items  
- `order`, `order_details`, `order_history`, `order_status`, `order_archive` → Full order lifecycle (from placement to archive)  

###  Payments
- `payment_setting` → User preferences for payment methods  
- `transaction`, `transaction_details`, `transaction_status` → Core payment tracking  
- `payment_integration_type`, `payment_integration_configuration` → 3rd-party gateway setup  
- `transaction_history`, `auditing` → Logs for compliance and debugging  

###  Customers
- `customer` → Customer-specific profile  
- `address` → Multiple delivery addresses  
- `inquiry` → Customer support tickets  
- `review`, `comments` → Feedback & ratings  

###  Offers & Promotions
- `offers`, `promotions` → Discounts, coupons, campaigns  

###  Reports & Analytics
- `system_reports`, `restaurant_reports`, `daily_transactions`, `monthly_transactions` → Aggregated reporting  

---
##  Database Relationships – Simple Overview

- **Customer — Order (1 : M)**  
  A customer can place multiple orders.

- **Order — Order_Detail (1 : M)**  
  Each order can contain multiple items.

- **Menu_Item — Order_Detail (1 : M)**  
  A menu item can appear in multiple orders.

- **Restaurant — Menu_Item (1 : M)**  
  Each restaurant can have multiple menu items.

- **Order — Payment (1 : 1 )**  
  Typically one payment per order

- **Customer — Address (1 : M)**  
  A customer can register multiple delivery addresses.
 ---
##   Diagrams 

The  process is documented using  different diagram types to illustrate it from multiple perspectives:  


##  System Scheme
An overview of the entire food delivery system architecture.

<img width="2106" height="1648" alt="food-deliver-system-scheme" src="https://github.com/user-attachments/assets/d1f52700-420f-4656-91fe-7f431a93a394" />

##  ERD (Entity Relationship Diagram)
Shows the relationships between the main entities in the food delivery system.

<img width="1328" height="912" alt="place-order-erd" src="https://github.com/user-attachments/assets/7ce9df00-1f63-430a-8915-b86be4d7f651" />

---

##  Flowcharts

### 1. Authentication Flowchart
The workflow for user authentication (login/signup).

<img width="311" height="291" alt="Auth flowchart" src="https://github.com/user-attachments/assets/8d1cba0a-4cea-4290-84d4-1e23983a7e0f" />

<img width="490" height="368" alt="Auth flowchart (2)" src="https://github.com/user-attachments/assets/0369f45b-f9c1-44b4-80aa-502a065c418b" />

### 2. Checkout Order Flowchart
The workflow from cart to order confirmation.

<img width="833" height="842" alt="checkout-order-flowchart" src="https://github.com/user-attachments/assets/bf8705ef-79b2-4767-a7bd-3e287b7fdda8" />

---

##  Sequence Diagram
The step-by-step interaction between **User**, **System**, and **API Endpoints** during the order creation process.

<img width="917" height="1201" alt="place-order-sequence" src="https://github.com/user-attachments/assets/ae1c89ab-13f2-46dd-8f27-363dae152720" />

---


### 📐Place order Sequence Diagram  
Describes the step-by-step interaction between the **User**, **System**, and **API Endpoints** during the order creation and processing flow.  

![Place Order Sequence Diagram](https://github.com/JanaMohamed42/Food-Delivery-System/blob/main/sequence%20Diagram%20place%20order.png)  

---
## 🛒 Place Order – Pseudocode

// 1. Validate Cart
IF NOT ValidateCart(cartItems) THEN
    RETURN "Error: Some items are out of stock"
END IF

// 2. Create new order in Pending state
orderId = CreateOrder(customerId, cartItems, status="Pending")

// 3. Check payment method
IF paymentMethod == "Online" THEN
    
    // 3.1 Call Payment Gateway
    paymentResult = SendPaymentRequest(orderId, cartItems.totalAmount)

    IF paymentResult == "Success" THEN
        UpdateOrderStatus(orderId, "Paid")
        ReduceStock(cartItems)
        RETURN "Order Confirmed: Payment Successful"
    ELSE
        UpdateOrderStatus(orderId, "Failed")
        RETURN "Error: Payment Failed"
    END IF

ELSE IF paymentMethod == "CashOnDelivery" THEN
    
    // 3.2 Record Payment as Pending (Cash)
    InsertPaymentRecord(orderId, method="Cash", status="Pending")
    RETURN "Order Confirmed: Pay with Cash on Delivery"

ELSE
    RETURN "Error: Invalid Payment Method"
END IF


