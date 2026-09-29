# ShopSphere — Java Full-Stack E-Commerce

A portfolio-ready e-commerce application built with Java, Spring Boot, Spring Data JPA, MySQL, and a responsive HTML/CSS/JavaScript frontend.

## Features
- Product catalogue and search
- Product details
- Category filtering
- Shopping cart
- REST APIs
- MySQL persistence
- Seed products for local development
- Responsive frontend served by Spring Boot
- Global exception handling and validation

## Tech Stack
Java 17+, Spring Boot 3, Spring Web, Spring Data JPA, MySQL, Maven, HTML5, CSS3, JavaScript.

## Run
1. Create a MySQL database named `shopsphere`.
2. Update database credentials in `src/main/resources/application.properties`.
3. Run `mvn spring-boot:run`.
4. Open `http://localhost:8080`.

API base: `/api/products`

This project is intentionally structured so authentication, orders, payments, and an admin dashboard can be added as the next development phase.
