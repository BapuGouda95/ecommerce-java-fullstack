package com.shopsphere.config;

import com.shopsphere.product.Product;
import com.shopsphere.product.ProductRepository;
import com.shopsphere.user.Role;
import com.shopsphere.user.User;
import com.shopsphere.user.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.math.BigDecimal;

@Configuration
public class DataSeeder {
    @Bean
    CommandLineRunner seed(ProductRepository products, UserRepository users, PasswordEncoder encoder) {
        return args -> {
            if (products.count() == 0) {
                products.save(new Product("AeroSound Wireless Headphones","Electronics",new BigDecimal("2499.00"),"Comfortable wireless headphones with deep bass and long battery life.","https://images.unsplash.com/photo-1505740420928-5e560c06d30e",25));
                products.save(new Product("Nova Mechanical Keyboard","Electronics",new BigDecimal("3299.00"),"Compact mechanical keyboard designed for work and gaming.","https://images.unsplash.com/photo-1587829741301-dc798b83add3",18));
                products.save(new Product("Urban Classic Sneakers","Fashion",new BigDecimal("2199.00"),"Minimal everyday sneakers with a lightweight sole.","https://images.unsplash.com/photo-1542291026-7eec264c27ff",30));
                products.save(new Product("Metro Backpack","Accessories",new BigDecimal("1599.00"),"Water-resistant laptop backpack with organized storage.","https://images.unsplash.com/photo-1553062407-98eeb64c6a62",20));
            }
            String email = System.getenv().getOrDefault("ADMIN_EMAIL", "admin@shopsphere.local").toLowerCase();
            if (users.findByEmailIgnoreCase(email).isEmpty()) {
                String password = System.getenv().getOrDefault("ADMIN_PASSWORD", "Admin@12345");
                users.save(new User("ShopSphere Admin", email, encoder.encode(password), Role.ADMIN));
            }
        };
    }
}
