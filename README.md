# ShopSphere — Java Full-Stack E-Commerce

ShopSphere is a portfolio-ready e-commerce application built with Java, Spring Boot, Spring Data JPA, Spring Security, JWT authentication, MySQL and a responsive HTML/CSS/JavaScript frontend.

## Features
- Product catalogue, search and category filtering
- Product CRUD APIs with admin-only write access
- User registration and BCrypt password hashing
- JWT login with USER/ADMIN roles
- Persistent orders and order items
- Stock validation and automatic stock deduction
- Customer order history
- Admin order listing and status updates
- Global validation/error handling
- MySQL persistence
- Responsive frontend with cart stored in localStorage

## Tech stack
Java 17, Spring Boot 3.5.6, Spring Security, Spring Data JPA, MySQL, Maven, HTML5, CSS3, JavaScript

## Run locally
1. Create/use MySQL. The application can create the shopsphere database automatically.
2. Update application.properties with your MySQL username/password.
3. Replace app.jwt.secret with a long random secret.
4. Run: mvn spring-boot:run
5. Open http://localhost:8080

## APIs
- POST /api/auth/register
- POST /api/auth/login
- GET /api/products
- GET /api/products/{id}
- POST/PUT/DELETE /api/products — ADMIN
- POST /api/orders — logged-in user
- GET /api/orders/my — logged-in user
- GET /api/orders — ADMIN
- PATCH /api/orders/{id}/status?status=CONFIRMED — ADMIN

## Security
Passwords are stored using BCrypt. Login returns a signed JWT. Send it using the Authorization: Bearer <token> header.

Never commit real database passwords, JWT secrets, API keys or payment credentials.

## Roadmap
Admin dashboard, checkout/address workflow, payment integration, automated tests and Docker deployment.
