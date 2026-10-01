package com.shopsphere.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.net.URI;

@Configuration
public class DatabaseConfig {

    @Bean
    HikariDataSource dataSource(Environment env) {
        String rawUrl = env.getRequiredProperty("DB_URL");
        String username = env.getRequiredProperty("DB_USERNAME");
        String password = env.getRequiredProperty("DB_PASSWORD");

        if (rawUrl.startsWith("jdbc:")) {
            HikariConfig config = new HikariConfig();
            config.setJdbcUrl(rawUrl);
            config.setUsername(username);
            config.setPassword(password);
            return new HikariDataSource(config);
        }

        URI uri = URI.create(rawUrl);
        String scheme = uri.getScheme();
        if (!"postgresql".equalsIgnoreCase(scheme) && !"postgres".equalsIgnoreCase(scheme)) {
            throw new IllegalArgumentException("Unsupported database URL scheme: " + scheme);
        }

        StringBuilder jdbcUrl = new StringBuilder("jdbc:postgresql://")
                .append(uri.getHost());

        if (uri.getPort() != -1) {
            jdbcUrl.append(":").append(uri.getPort());
        }

        jdbcUrl.append(uri.getPath());

        if (uri.getQuery() != null && !uri.getQuery().isBlank()) {
            jdbcUrl.append("?").append(uri.getQuery());
        }

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(jdbcUrl.toString());
        config.setUsername(username);
        config.setPassword(password);
        return new HikariDataSource(config);
    }
}
