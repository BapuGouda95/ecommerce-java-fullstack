# ShopSphere — Java Full-Stack E-Commerce

ShopSphere is a portfolio-ready e-commerce application built with Java 17, Spring Boot, Spring Data JPA, Spring Security, JWT authentication, MySQL and a responsive HTML/CSS/JavaScript frontend.

## Features
- Product catalogue with search and category filtering
- Stock visibility and validation
- User registration/login with BCrypt + JWT
- USER / ADMIN role-based authorization
- Browser-persisted shopping cart
- Protected checkout with delivery address
- Persistent orders and order items
- Automatic stock deduction
- Customer order history
- Admin dashboard
- Admin product create/update/delete
- Admin order listing and status management
- Validation and global API error handling
- Unit tests
- Docker + Docker Compose
- GitHub Actions Maven CI

## Tech stack
Java 17 · Spring Boot 3.5.6 · Spring Security · Spring Data JPA · MySQL 8 · Maven · HTML5 · CSS3 · JavaScript

## Run locally
1. Create/use MySQL.
2. Configure DB_USERNAME, DB_PASSWORD and optionally DB_URL.
3. Set JWT_SECRET to a random value of at least 32 characters.
4. Run mvn spring-boot:run.
5. Open http://localhost:8080.

## Docker
Run docker compose up --build, then open http://localhost:8080.

## Demo administrator
For local/demo use, the seeder creates an administrator if the configured admin email does not already exist.

Default email: admin@shopsphere.local
Default password: Admin@12345

Change these with ADMIN_EMAIL and ADMIN_PASSWORD environment variables before deployment.

## Main APIs
- POST /api/auth/register
- POST /api/auth/login
- GET /api/products
- GET /api/products/{id}
- POST/PUT/DELETE /api/products — ADMIN
- POST /api/orders — authenticated USER
- GET /api/orders/my — authenticated USER
- GET /api/orders — ADMIN
- PATCH /api/orders/{id}/status?status=CONFIRMED — ADMIN

## Verification
Run mvn test. GitHub Actions also runs the Maven test suite on pushes and pull requests to main.

## Security
Passwords are BCrypt-hashed and JWTs use HMAC-SHA256. Do not commit real database passwords, JWT secrets, payment credentials or production administrator passwords.

Checkout is intentionally a simulated portfolio checkout; it does not process real payments.
