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
## Features & API Endpoints

| # | Feature               | Functionalities                                                                                                    | Resource(s)                 | Example API Endpoints                                                                                       |
|---|-----------------------|--------------------------------------------------------------------------------------------------------------------|-----------------------------|-------------------------------------------------------------------------------------------------------------|
| 1 | **User Management**   | - Register new user<br>- Login / Logout<br>- View & edit profile<br>- Manage account details<br>- Set delivery address | `users`, `sessions`, `addresses` | - `POST /users` → Register<br>- `POST /sessions/login` → Login<br>- `POST /sessions/logout` → Logout<br>- `GET /users/{id}` → View profile<br>- `PUT /users/{id}` → Edit profile<br>- `DELETE /users/{id}` → Delete account<br>- `POST /users/{id}/addresses` → Add address |
| 2 | **Browse Restaurants**| - View all restaurants<br>- View restaurant details                                                                | `restaurants`               | - `GET /restaurants` → List restaurants<br>- `GET /restaurants/{id}` → Restaurant details                   |
| 3 | **Menu Browsing**     | - View menu for a restaurant<br>- View menu item details                                                           | `menus`, `menu-items`       | - `GET /restaurants/{id}/menus` → Restaurant menus<br>- `GET /menus/{id}/menu-items` → Menu items<br>- `GET /menu-items/{id}` → Item details |
| 4 | **Shopping Cart**     | - View cart<br>- Add item to cart<br>- Update item quantity<br>- Remove item<br>- Clear cart                        | `cart`, `cart-items`        | - `GET /cart` → View cart<br>- `POST /cart-items` → Add item<br>- `PUT /cart-items/{id}` → Update quantity<br>- `DELETE /cart-items/{id}` → Remove item<br>- `DELETE /cart` → Empty cart |
| 5 | **Order Management**  | - Place new order<br>- Review order summary<br>- Confirm checkout<br>- View single order<br>- List all user orders   | `orders`                    | - `POST /orders` → Place order<br>- `GET /orders/{id}` → Order details<br>- `GET /orders` → List user orders |
| 6 | **Payments**          | - Choose payment method (Cash, Card, Wallet)<br>- Enter payment details<br>- Execute payment<br>- View payment status<br>- Refund payment | `payments`                  | - `POST /payments` → Start payment<br>- `PUT /payments/{id}/method` → Choose method<br>- `PUT /payments/{id}/details` → Add payment details<br>- `POST /payments/{id}/process` → Execute payment<br>- `GET /payments/{id}` → Payment details<br>- `POST /payments/{id}/refund` → Refund payment |
| 7 | **Delivery Tracking** | - Request delivery<br>- Assign delivery person<br>- Track delivery status                                           | `deliveries`                | - `POST /deliveries` → Create delivery<br>- `PUT /deliveries/{id}/assign` → Assign delivery<br>- `GET /deliveries/{id}` → Delivery status |
