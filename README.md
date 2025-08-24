# Javeats Lite - Food Delivery System

> Javeats Lite is a simplified food delivery platform concept that connects customers with restaurants, enabling browsing, ordering, payments, and delivery tracking.  

---

## Notes
- **Feature**: A high-level capability of the system derived from the use case diagram. Each feature represents a core business area of the platform.
- **Functionalities**: Specific actions or operations that can be performed under each feature to achieve a user goal.
- **Resource**: The main data entity or object handled by the system, typically mapped to a database table or API model.
- **API Endpoint**: A specific URL pattern combined with an HTTP method (GET, POST, PUT, DELETE) used to interact with a resource.

---

## Future Work
- Implement backend REST API using Node.js / Express.
- Create a responsive frontend interface for customers and restaurants.
- Integrate with real-world payment gateways (e.g., Stripe, PayPal).
- Add real-time delivery tracking with WebSockets.
- Implement authentication and authorization with JWT.

---
# 🍔 Food Delivery System – API Design

This project defines the **functional requirements, database entities, and API endpoints** for a food delivery application.  
It covers the entire lifecycle: from **user registration and authentication**, browsing restaurants, managing menus, handling carts & orders, to **payments, offers, and dashboards**.

---

## 🚀 Features & Functionalities

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

## 📂 Database Entities (Detailed Overview)

The database is divided into **modules** for clarity:

### 👤 Users & Roles
- `user` → Basic user details (name, email, password, phone)  
- `role` → Defines system roles (Admin, Manager, Customer)  
- `user_role`, `group`, `group_role`, `user_group` → Manage role-based access and group permissions  

### 🍴 Restaurants & Menus
- `restaurant` → Core details (name, address, status)  
- `restaurant_category` → Classifies restaurants by cuisine/type  
- `menu`, `menu_category`, `menu_item`, `ingredient` → Full menu hierarchy  

### 🛒 Cart & Orders
- `cart`, `cart_items` → User’s current cart and its items  
- `order`, `order_details`, `order_history`, `order_status`, `order_archive` → Full order lifecycle (from placement to archive)  

### 💳 Payments
- `payment_setting` → User preferences for payment methods  
- `transaction`, `transaction_details`, `transaction_status` → Core payment tracking  
- `payment_integration_type`, `payment_integration_configuration` → 3rd-party gateway setup  
- `transaction_history`, `auditing` → Logs for compliance and debugging  

### 👥 Customers
- `customer` → Customer-specific profile  
- `address` → Multiple delivery addresses  
- `inquiry` → Customer support tickets  
- `review`, `comments` → Feedback & ratings  

### 🎁 Offers & Promotions
- `offers`, `promotions` → Discounts, coupons, campaigns  

### 📊 Reports & Analytics
- `system_reports`, `restaurant_reports`, `daily_transactions`, `monthly_transactions` → Aggregated reporting  

---

## 📦 Place Order – Diagrams  

The **Place Order** process is documented using three different diagram types to illustrate it from multiple perspectives:  

### 🗂️ ERD (Entity Relationship Diagram)  
Shows the relationships between the main entities involved in placing an order (`order`, `order_items`, `customer`, `payment`, etc.).  

![Place Order ERD](https://raw.githubusercontent.com/USERNAME/REPO-NAME/main/assets/ERD%20Diagram_place_order.png)
---

### 🔄 Flowchart  
Represents the overall workflow starting from when the customer places a new order until the restaurant confirms or cancels it.  

![Place Order Flowchart](https://raw.githubusercontent.com/USERNAME/REPO-NAME/main/assets/Flowchart%20Diagram_place_order.png)

---

### 📐 Sequence Diagram  
Describes the step-by-step interaction between the **User**, **System**, and **API Endpoints** during the order creation and processing flow.  

![Place Order Sequence Diagram](https://raw.githubusercontent.com/USERNAME/REPO-NAME/main/assets/Sequence%20Diagram_place_order.png)


